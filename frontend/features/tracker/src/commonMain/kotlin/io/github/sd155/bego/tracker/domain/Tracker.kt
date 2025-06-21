package io.github.sd155.bego.tracker.domain

import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.di.trackerModuleName
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.cos
import kotlin.math.sqrt

internal class Tracker {
    private val _logger by lazy { Inject.instance<Logger>(trackerModuleName) }
    private val _scope by lazy { CoroutineScope(Dispatchers.Default) }
    private val _stopwatch by lazy { Stopwatch() }
    private val _location by lazy { Inject.instance<LocationProvider>() }
    private val _locationFilter by lazy { KalmanLocationFilter(processNoise = 1.0) }
    private val _state = MutableStateFlow(TrackerState())
    internal val state: StateFlow<TrackerState> = _state.asStateFlow()

    init {
        _stopwatch.state
            .onEach(::handleStopwatchState)
            .launchIn(_scope)
    }

    internal suspend fun prepare() {
        _location.start()
    }

    private fun handleStopwatchState(state: StopwatchState) {
        _state.value = _state.value.copy(
            time = state.elapsedMs,
            running = state.isRunning
        )
    }

    private fun handleTrackPoint(point: TrackPoint) {
        _state.value.let { state ->
            val filteredPoint = _locationFilter.filter(point)
            state.last
                ?.let { last ->
                    val distance = approximateDistance(last, filteredPoint)
                    _logger.debug("Distance check, speed: ${filteredPoint.speedMetersPerSecond}[${point.speedMetersPerSecond}]m/s, distance:${distance}m ? accuracy:${last.horizontalAccuracyMeters}m")
                    if (filteredPoint.speedMetersPerSecond > 0f && distance > last.horizontalAccuracyMeters) {
                        state.copy(
                            distance = state.distance + distance,
                            last = filteredPoint
                        )
                            .apply {
                                _state.value = this
                                if (this.distance >= 1000.0) stop()
                            }
                    }
                }
                ?: run {
                    _logger.debug(event = "First point received")
                    _state.value = state.copy(last = filteredPoint)
                }
        }
    }

    internal suspend fun start() {
        _stopwatch.start()
        _location.sub(::handleTrackPoint)
    }

    internal fun stop() {
        _stopwatch.stop()
        _location.unsub()
    }

    internal fun reset() {
        _stopwatch.reset()
        _location.unsub()
        _locationFilter.reset()
        _state.value = TrackerState()
    }

    /**
     * That is simplified flat-Earth approximation.
     * That one has accuracy like Haversine but faster.
     * That one loosing a little in accuracy to Vincenty, but wins great in speed.
     */
    private fun approximateDistance(p1: TrackPoint, p2: TrackPoint): Double {
        val earthRadiusMeters = 6371000.0
        val latAvg = Math.toRadians((p1.latitudeDegrees + p2.latitudeDegrees) / 2)
        val x = Math.toRadians(p2.longitudeDegrees - p1.longitudeDegrees) * cos(latAvg)
        val y = Math.toRadians(p2.latitudeDegrees - p1.latitudeDegrees)
        return sqrt(x * x + y * y) * earthRadiusMeters
    }
}
