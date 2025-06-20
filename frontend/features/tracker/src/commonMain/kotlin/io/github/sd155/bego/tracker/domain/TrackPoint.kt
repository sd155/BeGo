package io.github.sd155.bego.tracker.domain

internal data class TrackPoint(
    val timeMs: Long,
    val latitudeDegrees: Double,
    val longitudeDegrees: Double,
    val horizontalAccuracyMeters: Float,
    val altitudeMeters: Double,
    val altitudeAccuracyMeters: Float,
    val speedMetersPerSecond: Float,
    val speedAccuracyMeterPerSecond: Float,
    val bearingDegrees: Float,
    val bearingAccuracyDegrees: Float,
)