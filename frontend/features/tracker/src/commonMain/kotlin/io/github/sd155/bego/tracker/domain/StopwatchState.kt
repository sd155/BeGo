package io.github.sd155.bego.tracker.domain

internal data class StopwatchState(
    val elapsedMs: Long = 0L,
    val isRunning: Boolean = false,
)