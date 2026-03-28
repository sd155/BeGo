package io.github.sd155.bego.tracker.ui

import io.github.sd155.bego.tracker.domain.PlatformReason

internal sealed class TrackerViewState {
    data object Initialization : TrackerViewState()
    data object FatalInitializationError : TrackerViewState()
    data class PlatformNotReady(
        val reason: PlatformReason,
    ) : TrackerViewState()
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

