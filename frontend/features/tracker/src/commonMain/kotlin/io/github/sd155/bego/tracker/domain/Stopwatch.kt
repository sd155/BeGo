package io.github.sd155.bego.tracker.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.TimeSource

internal class Stopwatch {
    private val _timer by lazy { Timer(_resolutionMs = 10L, ::onTimerUpdate) }
    private val _state = MutableStateFlow(StopwatchState())
    internal val state: StateFlow<StopwatchState> = _state.asStateFlow()

    internal fun startLap() {
        _state.value.reduce {
            copy(
                isRunning = true,
                currentLapStartMs = currentStartMs + elapsedMs,
            )
        }
            .also { oldState ->
                if (!oldState.isRunning) _timer.start()
            }
    }

    internal fun pause() {
        _timer.stop()
        _state.value.reduce {
            copy(
                isRunning = false,
                currentStartMs = currentStartMs + elapsedMs,
                elapsedMs = 0L,
            )
        }
    }

    internal fun resume() {
        _state.value.reduce {
            copy(isRunning = true)
        }
        _timer.start()
    }

    internal fun reset() {
        _timer.stop()
        _state.value.reduce { StopwatchState() }
    }

    private fun onTimerUpdate(elapsedMs: Long) {
        _state.value.reduce { copy(elapsedMs = elapsedMs) }
    }

    private fun StopwatchState.reduce(reducer: StopwatchState.() -> StopwatchState): StopwatchState {
        _state.value = reducer(this)
        return this
    }
}

internal data class StopwatchState(
    val currentStartMs: Long = 0L,
    val currentLapStartMs: Long = 0L,
    val elapsedMs: Long = 0L,
    val isRunning: Boolean = false,
)

@OptIn(ExperimentalAtomicApi::class)
private class Timer(
    private val _resolutionMs: Long,
    private val _onUpdate: (elapsedMs: Long) -> Unit,
) {
    private val _scope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    private val _job by lazy { AtomicReference<Job?>(value = null) }

    fun start() {
        stop()
        _job.store(_scope.launch {
            val start = TimeSource.Monotonic.markNow()
            while (true) {
                delay(timeMillis = _resolutionMs)
                val elapsedMs = start.elapsedNow().inWholeMilliseconds
                _onUpdate(elapsedMs)
            }
        })
    }

    fun stop() =
        _job.load()?.cancel()
}