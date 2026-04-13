package io.github.sd155.bego.tracker.domain

import io.github.sd155.bego.tracker.app.LocationProvider
import io.github.sd155.bego.utils.Result
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sqrt

internal class Tracker(
    private val logger: Logger,
    private val locationProvider: LocationProvider,
) {
    private val _minPlausibleSegmentMeters = 0.25
    private val _minSpeedDistanceMultiplier = 0.3
    private val _minHighSpeedDistanceMultiplier = 0.5
    private val _highSpeedThresholdMetersPerSecond = 3.0f
    private val _maxSpeedDistanceMultiplier = 2.5
    private val _maxSpeedDistanceSlackMeters = 1.0
    private val _scope by lazy { CoroutineScope(Dispatchers.Default) }
    private val _stopwatch by lazy { Stopwatch() }
    private val _locationFilter by lazy { KalmanLocationFilter(processNoise = 1.0) }
    private val _state = MutableStateFlow(TrackerState())
    internal val state: StateFlow<TrackerState> = _state.asStateFlow()

    init {
        _stopwatch.state
            .onEach(::handleStopwatchState)
            .launchIn(_scope)
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
                    val lastSegmentDistance = approximateDistance(last, filteredPoint)
                    val deltaTimeSeconds = ((filteredPoint.timeMs - last.timeMs).coerceAtLeast(0)).toDouble() / 1000.0
                    val minPlausibleSegmentMeters = minPlausibleSegmentMeters(
                        speedMetersPerSecond = filteredPoint.speedMetersPerSecond,
                        deltaTimeSeconds = deltaTimeSeconds,
                    )
                    val maxPlausibleSegmentMeters = maxPlausibleSegmentMeters(
                        speedMetersPerSecond = filteredPoint.speedMetersPerSecond,
                        deltaTimeSeconds = deltaTimeSeconds,
                    )
                    logger.debug("Distance check, speed: ${filteredPoint.speedMetersPerSecond}[${point.speedMetersPerSecond}]m/s, distance:${state.distance}+${lastSegmentDistance}m ? plausible:${minPlausibleSegmentMeters}..${maxPlausibleSegmentMeters}m")
                    if (
                        filteredPoint.speedMetersPerSecond > 0f &&
                        lastSegmentDistance >= minPlausibleSegmentMeters &&
                        lastSegmentDistance <= maxPlausibleSegmentMeters
                    ) {
                        val distance = state.distance + lastSegmentDistance
                        val speed = calculateSpeedKph(distance, state.time)
                        val pace = calculatePaceMsPerKm(distance, state.time)
                        state.copy(
                            distance = distance,
                            speed = speed,
                            pace = pace,
                            last = filteredPoint
                        )
                            .apply {
                                _state.value = this
                                if (this.distance >= this.finish) stop()
                            }
                    }
                    else {
                        _state.value = state.copy(last = filteredPoint)
                    }
                }
                ?: run {
                    logger.debug(event = "First point received")
                    _state.value = state.copy(last = filteredPoint)
                }
        }
    }

    private fun calculateSpeedKph(distanceMeters: Double, timeMs: Long): Float =
        if (distanceMeters > 0.0 && timeMs > 0L)
            (distanceMeters / (timeMs / 1000f) * 3.6).toFloat()
        else
            0f

    private fun calculatePaceMsPerKm(distanceMeters: Double, timeMs: Long): Long =
        if (distanceMeters > 0.0 && timeMs > 0L)
            ((timeMs.toDouble() / distanceMeters) * 1000.0).toLong()
        else
            0L

    private fun minPlausibleSegmentMeters(
        speedMetersPerSecond: Float,
        deltaTimeSeconds: Double,
    ): Double {
        if (speedMetersPerSecond <= 0f || deltaTimeSeconds <= 0.0) return _minPlausibleSegmentMeters
        val speedDistanceMultiplier =
            if (speedMetersPerSecond >= _highSpeedThresholdMetersPerSecond) _minHighSpeedDistanceMultiplier
            else _minSpeedDistanceMultiplier
        return max(
            _minPlausibleSegmentMeters,
            speedMetersPerSecond * deltaTimeSeconds * speedDistanceMultiplier
        )
    }

    private fun maxPlausibleSegmentMeters(
        speedMetersPerSecond: Float,
        deltaTimeSeconds: Double,
    ): Double {
        if (speedMetersPerSecond <= 0f) return 0.0
        if (deltaTimeSeconds <= 0.0) return _maxSpeedDistanceSlackMeters
        return speedMetersPerSecond * deltaTimeSeconds * _maxSpeedDistanceMultiplier + _maxSpeedDistanceSlackMeters
    }

    internal suspend fun start(): Result<LocationError, Unit> =
        locationProvider.sub(onUpdate = ::handleTrackPoint)
            .withSuccess { _stopwatch.start() }

    internal fun stop() {
        _stopwatch.stop()
        locationProvider.unsub()
    }

    internal fun reset() {
        _stopwatch.reset()
        locationProvider.unsub()
        _locationFilter.reset()
        _state.value = TrackerState()
    }

    internal fun setTargetDistance(distance: Double) {
        if (!_state.value.running) {
            _state.value = _state.value.copy(finish = distance)
        }
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
