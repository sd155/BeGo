package io.github.sd155.bego.tracker.ui

internal sealed class TrackerViewIntent {
    data object Start : TrackerViewIntent()
    data object Prepare : TrackerViewIntent()
    data object Stop : TrackerViewIntent()
    data object Reset : TrackerViewIntent()
}