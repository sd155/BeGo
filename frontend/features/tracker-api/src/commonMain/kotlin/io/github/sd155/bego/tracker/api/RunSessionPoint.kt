package io.github.sd155.bego.tracker.api

data class RunSessionPoint(
    val sessionStartTimeMs: Long,
    val sessionDurationMs: Long,
    val targetDistanceMeters: Double,
    val sessionDistanceMeters: Double,
    val latitudeDegrees: Double,
    val longitudeDegrees: Double,
    val altitudeMeters: Double,
    val bearingDegrees: Float,
    val speedMetersPerSecond: Float,
    val averageSpeedKph: Float,
    val averagePaceMsPerKm: Long,
) {
    init {
        require(sessionStartTimeMs > 0L) { "Time must be greater than zero" }
        require(latitudeDegrees in -90.0..90.0) { "Latitude must be in range [-90, 90]" }
        require(longitudeDegrees in -180.0..180.0) { "Longitude must be in range [-180, 180]" }
        require(speedMetersPerSecond >= 0f) { "Speed must not be negative" }
        require(averageSpeedKph >= 0f) { "Average speed must not be negative" }
        require(bearingDegrees in 0f..360f) { "Bearing must be in range [0-360] degrees" }
        require(averagePaceMsPerKm >= 0L) { "Pace must not be negative" }
        require(sessionDurationMs >= 0L) { "Duration time must not be negative" }
        require(sessionDistanceMeters >= 0.0) { "Distance must not be negative" }
        require(targetDistanceMeters >= 0.0) { "Target distance must not be negative" }
    }
}
