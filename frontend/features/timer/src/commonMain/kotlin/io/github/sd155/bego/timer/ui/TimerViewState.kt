package io.github.sd155.bego.timer.ui

internal sealed class TimerViewState {
    data class Initial(
        val totalTime: String,
    ) : TimerViewState()
    data class RunningNoLaps(
        val totalTime: String,
    ) : TimerViewState()
    data class StoppedNoLaps(
        val totalTime: String,
    ) : TimerViewState()
    data class RunningWithLaps(
        val totalTime: String,
        val currentLapTime: String,
        val laps: List<LapItem> = emptyList()//TODO: remove default value
    ) : TimerViewState()
    data class StoppedWithLaps(
        val totalTime: String,
        val currentLapTime: String,
        val laps: List<LapItem> = emptyList()//TODO: remove default value
    ) : TimerViewState()
}

internal data class LapItem(
    val lapNumber: Int,
    val lapTimeCs: Long,
    val onLapTotalTimeCs: Long,
) 