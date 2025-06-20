package io.github.sd155.bego.tracker.domain

import kotlin.math.*

private data class Data(
    val latitude: Double,
    val longitude: Double,
    val speed: Float,
    val bearing: Float,
    val timeMs: Long,
    val variance: Double, // meters^2
)

/**
 * Kalman filter for GPS smoothing with constant velocity model.
 */
internal class KalmanLocationFilter(
    private val processNoise: Double = 1.0, // meters^2, tune for responsiveness
    private val speedThreshold: Float = 0.5f, // meters per second
) {
    private var _data: Data? = null

    fun filter(point: TrackPoint): TrackPoint {
        val accuracy2 = point.horizontalAccuracyMeters.toDouble().pow(2)
        val bearingAccuracy2 = point.bearingAccuracyDegrees.toDouble().pow(2)
        val measuredSpeed = if (point.speedMetersPerSecond < speedThreshold) 0f else point.speedMetersPerSecond
        return _data?.let { data ->
            // Prediction step: only predict movement if speed > threshold
            val deltaTimeSeconds = ((point.timeMs - data.timeMs).coerceAtLeast(0)).toDouble() / 1000.0
            val predictedData =
                if (deltaTimeSeconds > 0.0 && data.speed > speedThreshold) {
                    val distanceMeters = data.speed * deltaTimeSeconds
                    val earthRadiusMeters = 6371000.0
                    val bearingRadians = Math.toRadians(data.bearing.toDouble())
                    val latitudeRadians = Math.toRadians(data.latitude)
                    val longitudeRadians = Math.toRadians(data.longitude)
                    val newLatitudeRadians = asin(sin(latitudeRadians) * cos(distanceMeters / earthRadiusMeters) + cos(latitudeRadians) * sin(distanceMeters / earthRadiusMeters) * cos(bearingRadians))
                    val newLongitudeRadians = longitudeRadians + atan2(
                        sin(bearingRadians) * sin(distanceMeters / earthRadiusMeters) * cos(latitudeRadians),
                        cos(distanceMeters / earthRadiusMeters) - sin(latitudeRadians) * sin(newLatitudeRadians)
                    )
                    data.copy(
                        latitude = Math.toDegrees(newLatitudeRadians),
                        longitude = Math.toDegrees(newLongitudeRadians),
                        variance = data.variance + processNoise * deltaTimeSeconds
                    )
                }
                else {
                    data.copy(variance = data.variance + processNoise)
                }

            // Kalman gain
            val k = data.variance / (data.variance + accuracy2)
            // Update estimate with measurement (simple blend)
            val measuredData = data.copy(
                latitude = predictedData.latitude + k * (point.latitudeDegrees - predictedData.latitude),
                longitude = predictedData.longitude + k * (point.longitudeDegrees - predictedData.longitude),
                speed = measuredSpeed,
                bearing = predictedData.bearing + (point.bearingDegrees - predictedData.bearing) * (bearingAccuracy2 / (bearingAccuracy2 + 1.0)).toFloat(),
                timeMs = point.timeMs,
                variance = predictedData.variance * (1 - k)
            )
            _data = measuredData
            return point.copy(
                latitudeDegrees = measuredData.latitude,
                longitudeDegrees = measuredData.longitude,
                speedMetersPerSecond = measuredData.speed
            )
        }
            ?: let {
                // First measurement: initialize state
                _data = Data(
                    latitude = point.latitudeDegrees,
                    longitude = point.longitudeDegrees,
                    speed = measuredSpeed,
                    bearing = point.bearingDegrees,
                    timeMs = point.timeMs,
                    variance = accuracy2,
                )
                point.copy(speedMetersPerSecond = measuredSpeed)
            }
    }

    fun reset() {
        _data = null
    }
} 