package io.github.sd155.bego.tracker.ui

internal sealed class TrackerViewIntent {
    data object Start : TrackerViewIntent()
    data object Stop : TrackerViewIntent()
    data object Reset : TrackerViewIntent()
    data class SetTarget(val targetKm: Int) : TrackerViewIntent()
}