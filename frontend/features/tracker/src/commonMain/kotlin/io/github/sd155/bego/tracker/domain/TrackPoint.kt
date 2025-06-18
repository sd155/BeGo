package io.github.sd155.bego.tracker.domain

internal data class TrackPoint(
    val timeMs: Long,
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracyMeters: Float,
    val altitudeMeters: Double,
    val altitudeAccuracyMeters: Float,
    val speedMetersPerSecond: Float,
    val speedAccuracyMeterPerSecond: Float,
)