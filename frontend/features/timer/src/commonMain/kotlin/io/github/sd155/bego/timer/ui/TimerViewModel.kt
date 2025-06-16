package io.github.sd155.bego.timer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.timer.di.moduleName
import io.github.sd155.bego.timer.domain.RunningState
import io.github.sd155.bego.timer.domain.RunningTimer
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class TimerViewModel : ViewModel() {
    private val _logger by lazy { Inject.instance<Logger>(tag = moduleName) }
    private val _timer by lazy { Inject.instance<RunningTimer>() }
    private val _formatter by lazy { TimeFormatter() }
    private val _state = MutableStateFlow<TimerViewState>(TimerViewState.Initial(_formatter.format(timeMs = 0L)))
    internal val state: StateFlow<TimerViewState> = _state.asStateFlow()

    init {
        _timer.state.onEach(::collectTimerState).launchIn(viewModelScope)
    }

    internal fun onViewIntent(intent: TimerViewIntent) = viewModelScope.launch {
        _logger.debug("--->>> VIEW INTENT :::", diagnostics = listOf(intent))
        when (intent) {
            TimerViewIntent.StartTimer -> _timer.startLap()
            TimerViewIntent.StopTimer -> _timer.pause()
            TimerViewIntent.ContinueTimer -> _timer.resume()
            TimerViewIntent.ResetTimer -> _timer.reset()
            TimerViewIntent.NextLap -> _timer.startLap()
        }
    }

    private fun collectTimerState(state: RunningState) {
        _logger.trace("VM collected domain state", diagnostics = listOf(state))
        val totalTimeMs = state.currentStartMs + state.elapsedMs
        val totalTime = _formatter.format(totalTimeMs)
        val currentLapTime = _formatter.format(timeMs = totalTimeMs - state.currentLapStartMs)
        _state.value =
            if (state.isRunning) {
                if (state.isFirstLap())
                    TimerViewState.RunningNoLaps(totalTime = totalTime)
                else
                    TimerViewState.RunningWithLaps(
                        totalTime = totalTime,
                        currentLapTime = currentLapTime,
                    )
            }
            else {
                if (state.isInitial())
                    TimerViewState.Initial(totalTime = totalTime)
                else if (state.isFirstLap())
                    TimerViewState.StoppedNoLaps(totalTime = totalTime)
                else
                    TimerViewState.StoppedWithLaps(
                        totalTime = totalTime,
                        currentLapTime = currentLapTime,
                    )
            }
    }

    private fun RunningState.isFirstLap() =
        currentLapStartMs == 0L

    private fun RunningState.isInitial() =
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