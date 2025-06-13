package io.github.sd155.bego.timer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.TimeSource

internal class TimerViewModel : ViewModel() {
    private var timerJob: Job? = null
    private val _totalTimerStartCs = MutableStateFlow(value = 0L)
    private val _lapTimerStartCs = MutableStateFlow(value = 0L)
    private val _state = MutableStateFlow<TimerViewState>(TimerViewState.Initial)
    internal val state: StateFlow<TimerViewState> = _state.asStateFlow()

    internal fun onViewIntent(intent: TimerViewIntent) =
        when (intent) {
            TimerViewIntent.StartTimer -> startTimer()
            TimerViewIntent.StopTimer -> stopTimer()
            TimerViewIntent.ContinueTimer -> startTimer()
            TimerViewIntent.ResetTimer -> resetTimer()
            TimerViewIntent.NextLap -> startLap()
        }

    private fun startTimer() {
        _state.value = _state.value.toRunning()
        launchTimerJob()
    }

    private fun stopTimer() {
        stopTimerJob()
        _state.value = _state.value.toStopped()
    }

    private fun resetTimer() {
        stopTimerJob()
        _state.value = TimerViewState.Initial
    }

    private fun startLap() {
        _state.value = _state.value.toLaps()
    }

    private fun TimerViewState.toLaps(): TimerViewState =
        when (this) {
            is TimerViewState.RunningNoLaps -> {
                _lapTimerStartCs.value = totalTimeCs
                TimerViewState.RunningWithLaps(
                    totalTimeCs = totalTimeCs,
                    currentLapTimeCs = 0L,
                )
            }
            is TimerViewState.RunningWithLaps -> {
                _lapTimerStartCs.value = totalTimeCs
                copy(currentLapTimeCs = 0L)
            }
            else -> this
        }

    private fun TimerViewState.toStopped(): TimerViewState =
        when (this) {
            is TimerViewState.RunningNoLaps ->
                TimerViewState.StoppedNoLaps(
                    totalTimeCs = totalTimeCs
                )
            is TimerViewState.RunningWithLaps ->
                TimerViewState.StoppedWithLaps(
                    totalTimeCs = totalTimeCs,
                    currentLapTimeCs = currentLapTimeCs,
                )
            else -> this
        }

    private fun TimerViewState.toRunning(): TimerViewState =
        when (this) {
            TimerViewState.Initial -> {
                _totalTimerStartCs.value = 0L
                _lapTimerStartCs.value = 0L
                TimerViewState.RunningNoLaps()
            }
            is TimerViewState.StoppedNoLaps -> {
                _totalTimerStartCs.value = totalTimeCs
                TimerViewState.RunningNoLaps(
                    totalTimeCs = totalTimeCs
                )
            }
            is TimerViewState.StoppedWithLaps -> {
                _totalTimerStartCs.value = totalTimeCs
                TimerViewState.RunningWithLaps(
                    totalTimeCs = totalTimeCs,
                    currentLapTimeCs = currentLapTimeCs,
                )
            }
            else -> this
        }

    private fun TimerViewState.incrementTime(elapsedMs: Long): TimerViewState {
        val elapsedCs = elapsedMs / 10L
        val totalCs = _totalTimerStartCs.value + elapsedCs
        val lapCs = totalCs - _lapTimerStartCs.value
        return when (this) {
            is TimerViewState.RunningNoLaps ->
                copy(
                    totalTimeCs = totalCs
                )
            is TimerViewState.RunningWithLaps ->
                copy(
                    totalTimeCs = totalCs,
                    currentLapTimeCs = lapCs,
                )
            else -> this
        }
    }

    private fun launchTimerJob() {
        stopTimerJob()
        timerJob = viewModelScope.launch {
            val start = TimeSource.Monotonic.markNow()
            while (true) {
                delay(150L)
                val elapsedMs = start.elapsedNow().inWholeMilliseconds
                _state.value = _state.value.incrementTime(elapsedMs)
            }
        }
    }

    private fun stopTimerJob() =
        timerJob?.cancel()
}