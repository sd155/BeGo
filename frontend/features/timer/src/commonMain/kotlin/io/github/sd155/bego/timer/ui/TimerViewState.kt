package io.github.sd155.bego.timer.ui

/**
 * All time vals (Long) is in centi seconds
 */
internal sealed class TimerViewState {
    data object Initial : TimerViewState()
    data class RunningNoLaps(
        val totalTimeCs: Long = 0L,
    ) : TimerViewState()
    data class StoppedNoLaps(
        val totalTimeCs: Long,
    ) : TimerViewState()
    data class RunningWithLaps(
        val totalTimeCs: Long,
        val currentLapTimeCs: Long,
        val laps: List<LapItem>
    ) : TimerViewState()
    data class StoppedWithLaps(
        val totalTimeCs: Long,
        val currentLapTimeCs: Long,
        val laps: List<LapItem>
    ) : TimerViewState()
}

/**
 * All time vals (Long) is in centi seconds
 */
internal data class LapItem(
    val lapNumber: Int,
    val lapTimeCs: Long,
    val onLapTotalTimeCs: Long,
) 