package io.github.sd155.bego.tracker.domain

internal data class TrackerState(
    val finish: Double = 0.0,
    val distance: Double = 0.0,
    val time: Long = 0L,
    val speed: Float = 0f,
    val pace: Float = 0f,
    val last: TrackPoint? = null,
    val running: Boolean = false,
)