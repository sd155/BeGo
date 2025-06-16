package io.github.sd155.bego.tracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.di.moduleName
import io.github.sd155.bego.tracker.domain.StopwatchState
import io.github.sd155.bego.tracker.domain.Stopwatch
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class StopwatchViewModel : ViewModel() {
    private val _logger by lazy { Inject.instance<Logger>(tag = moduleName) }
    private val _stopwatch by lazy { Inject.instance<Stopwatch>() }
    private val _formatter by lazy { TimeFormatter() }
    private val _state = MutableStateFlow<StopwatchViewState>(StopwatchViewState.Initial(_formatter.format(timeMs = 0L)))
    internal val state: StateFlow<StopwatchViewState> = _state.asStateFlow()

    init {
        _stopwatch.state.onEach(::collectTimerState).launchIn(viewModelScope)
    }

    internal fun onViewIntent(intent: StopwatchViewIntent) = viewModelScope.launch {
        _logger.debug("--->>> VIEW INTENT :::", diagnostics = listOf(intent))
        when (intent) {
            StopwatchViewIntent.StartStopwatch -> _stopwatch.startLap()
            StopwatchViewIntent.StopStopwatch -> _stopwatch.pause()
            StopwatchViewIntent.ContinueStopwatch -> _stopwatch.resume()
            StopwatchViewIntent.ResetStopwatch -> _stopwatch.reset()
            StopwatchViewIntent.NextLap -> _stopwatch.startLap()
        }
    }

    private fun collectTimerState(state: StopwatchState) {
        _logger.trace("VM collected domain state", diagnostics = listOf(state))
        val totalTimeMs = state.currentStartMs + state.elapsedMs
        val totalTime = _formatter.format(totalTimeMs)
        val currentLapTime = _formatter.format(timeMs = totalTimeMs - state.currentLapStartMs)
        _state.value =
            if (state.isRunning) {
                if (state.isFirstLap())
                    StopwatchViewState.RunningNoLaps(totalTime = totalTime)
                else
                    StopwatchViewState.RunningWithLaps(
                        totalTime = totalTime,
                        currentLapTime = currentLapTime,
                    )
            }
            else {
                if (state.isInitial())
                    StopwatchViewState.Initial(totalTime = totalTime)
                else if (state.isFirstLap())
                    StopwatchViewState.StoppedNoLaps(totalTime = totalTime)
                else
                    StopwatchViewState.StoppedWithLaps(
                        totalTime = totalTime,
                        currentLapTime = currentLapTime,
                    )
            }
    }

    private fun StopwatchState.isFirstLap() =
        currentLapStartMs == 0L

    private fun StopwatchState.isInitial() =
        currentStartMs == 0L && elapsedMs == 0L && !isRunning


}

internal class TimeFormatter {

    internal fun format(timeMs: Long = 0L): String {
        val totalSeconds = timeMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val centiSeconds = (timeMs % 1000) / 10
        return "%02d : %02d . %02d".format(minutes, seconds, centiSeconds)
    }
}