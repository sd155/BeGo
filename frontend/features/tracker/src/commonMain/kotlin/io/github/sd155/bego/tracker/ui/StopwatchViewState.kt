package io.github.sd155.bego.tracker.ui

internal sealed class StopwatchViewState {
    data class Initial(
        val totalTime: String,
    ) : StopwatchViewState()
    data class RunningNoLaps(
        val totalTime: String,
    ) : StopwatchViewState()
    data class StoppedNoLaps(
        val totalTime: String,
    ) : StopwatchViewState()
    data class RunningWithLaps(
        val totalTime: String,
        val currentLapTime: String,
        val laps: List<LapItem> = emptyList()//TODO: remove default value
    ) : StopwatchViewState()
    data class StoppedWithLaps(
        val totalTime: String,
        val currentLapTime: String,
        val laps: List<LapItem> = emptyList()//TODO: remove default value
    ) : StopwatchViewState()
}

internal data class LapItem(
    val lapNumber: Int,
    val lapTimeCs: Long,
    val onLapTotalTimeCs: Long,
) 