package io.github.sd155.bego.tracker.ui

internal data class TrackerViewState(
    val time: String,
    val distance: String,
    val status: TrackerStatus,
)

internal enum class TrackerStatus { Initial, Running, Finished }