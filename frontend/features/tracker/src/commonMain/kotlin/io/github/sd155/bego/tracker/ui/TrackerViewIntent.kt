package io.github.sd155.bego.tracker.ui

import io.github.sd155.bego.tracker.app.LocationPrerequisites

internal sealed class TrackerViewIntent {
    data class Initialization(val prerequisites: LocationPrerequisites) : TrackerViewIntent()
    data object Start : TrackerViewIntent()
    data object Stop : TrackerViewIntent()
    data object Reset : TrackerViewIntent()
    data class SetTarget(val targetKm: Int) : TrackerViewIntent()
}
