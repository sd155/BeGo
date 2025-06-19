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

internal class TrackerViewModel : ViewModel() {
    private val _logger by lazy { Inject.instance<Logger>(tag = trackerModuleName) }
    private val _tracker by lazy { Inject.instance<Tracker>() }
    private val _formatter by lazy { UiFormatter() }
    private val _state = MutableStateFlow(TrackerViewState(
        time = _formatter.format(timeMs = 0L),
        distance = _formatter.format(distanceMeter = 0.0),
        TrackerStatus.Initial
    ))
    internal val state: StateFlow<TrackerViewState> = _state.asStateFlow()

    init {
        _tracker.state
            .onEach(::collectTrackerState)
            .launchIn(viewModelScope)
        viewModelScope.launch(Dispatchers.Default) { _tracker.prepare() }
    }

    private fun collectTrackerState(state: TrackerState) {
        _state.value = _state.value.copy(
            time = _formatter.format(timeMs = state.time),
            distance = _formatter.format(distanceMeter = state.distance),
            status =
                if (state.running) TrackerStatus.Running
                else if (state.time > 0L) TrackerStatus.Finished
                else TrackerStatus.Initial
        )
    }

    internal fun onViewIntent(intent: TrackerViewIntent) = viewModelScope.launch(Dispatchers.Default) {
        _logger.trace(event = "VM received View Intent: $intent")
        when (intent) {
            TrackerViewIntent.Start -> _tracker.start()
            TrackerViewIntent.Stop -> _tracker.stop()
            TrackerViewIntent.Reset -> _tracker.reset()
        }
    }
}

internal class UiFormatter {

    internal fun format(timeMs: Long = 0L): String {
        val totalSeconds = timeMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val centiSeconds = (timeMs % 1000) / 10
        return "%02d : %02d . %02d".format(minutes, seconds, centiSeconds)
    }

    internal fun format(distanceMeter: Double): String {
        val distance = distanceMeter.toInt()
        return "$distance"
    }
}