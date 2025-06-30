package io.github.sd155.bego.tracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.di.trackerModuleName
import io.github.sd155.bego.tracker.domain.Tracker
import io.github.sd155.bego.tracker.domain.TrackerState
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

internal class TrackerViewModel : ViewModel() {
    private val _logger by lazy { Inject.instance<Logger>(tag = trackerModuleName) }
    private val _tracker by lazy { Inject.instance<Tracker>() }
    private val _formatter by lazy { UiFormatter() }
    private val _state = MutableStateFlow(buildInitialState())
    internal val state: StateFlow<TrackerViewState> = _state.asStateFlow()

    companion object {
        private val _targetsKm = listOf(1, 5, 10, 21, 42)
    }

    init {
        _tracker.state
            .onEach(::collectTrackerState)
            .launchIn(viewModelScope.plus(Dispatchers.Default))
        viewModelScope.launch(Dispatchers.Default) {
            _tracker.prepare()
            _tracker.setTargetDistance(_targetsKm.first() * 1000.0)
        }
    }

    private fun collectTrackerState(state: TrackerState) {
        _state.value =
            if (state.running)
                TrackerViewState.Running(
                    target = _formatter.formatTarget(distanceMeters = state.finish),
                    time = _formatter.formatTime(timeMs = state.time),
                    pace = _formatter.formatPace(paceMpk = state.pace),
                    speed = _formatter.formatSpeed(speedKph = state.speed),
                    distance = _formatter.formatDistance(distanceMeters = state.distance),
                )
            else if (state.time > 0L)
                TrackerViewState.Finished(
                    time = _formatter.formatTime(timeMs = state.time),
                    pace = _formatter.formatPace(paceMpk = state.pace),
                    speed = _formatter.formatSpeed(speedKph = state.speed),
                    distance = _formatter.formatDistance(distanceMeters = state.distance),
                )
            else if (state.finish > 0.0)
                buildInitialState(target = state.finish.toInt() / 1000)
            else
                buildInitialState()
    }

    private fun buildInitialState(
        target: Int = _targetsKm.first()
    ): TrackerViewState =
        TrackerViewState.Initial(
            time = _formatter.formatTime(timeMs = 0L),
            targets = _targetsKm,
            selectedTarget = _targetsKm.find { targetKm -> target == targetKm } ?: _targetsKm.first()
        )

    internal fun onViewIntent(intent: TrackerViewIntent) = viewModelScope.launch(Dispatchers.Default) {
        _logger.trace(event = "VM received View Intent: $intent")
        when (intent) {
            TrackerViewIntent.Start -> _tracker.start()
            TrackerViewIntent.Stop -> _tracker.stop()
            TrackerViewIntent.Reset -> _tracker.reset()
            is TrackerViewIntent.SetTarget -> _tracker.setTargetDistance(intent.targetKm * 1000.0)
        }
    }
}

internal class UiFormatter {

    internal fun formatTime(timeMs: Long = 0L): String {
        val totalSeconds = timeMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val centiSeconds = (timeMs % 1000) / 10
        return "%02d : %02d . %02d".format(minutes, seconds, centiSeconds)
    }

    internal fun formatDistance(distanceMeters: Double): String {
        val distance = distanceMeters.toInt()
        return if (distance > 0) "$distance" else ""
    }

    internal fun formatTarget(distanceMeters: Double): Int {
        val distance = distanceMeters.toInt() / 1000
        return if (distance > 0) distance else 0
    }

    internal fun formatSpeed(speedKph: Float): String {
        return if (speedKph > 0f) "%.1f".format(speedKph) else ""
    }

    internal fun formatPace(paceMpk: Float): String {
        return if (paceMpk > 0f) {
            val minutes = paceMpk.toInt()
            val seconds = ((paceMpk - minutes) * 60).toInt()
            "%02d:%02d".format(minutes, seconds)
        }
        else {
            ""
        }
    }
}