package io.github.sd155.bego.tracker.ui

internal sealed class TrackerViewState {
    data class Initial(
        val time: String,
        val targets: List<Int>,
        val selectedTarget: Int,
    ) : TrackerViewState()
    data class Running(
        val time: String,
        val pace: String,
        val speed: String,
        val target: Int,
        val distance: String,
    ) : TrackerViewState()
    data class Finished(
        val time: String,
        val pace: String,
        val speed: String,
        val distance: String,
    ) : TrackerViewState()
}