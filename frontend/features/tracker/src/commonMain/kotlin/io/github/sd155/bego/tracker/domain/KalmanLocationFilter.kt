package io.github.sd155.bego.tracker.domain

import kotlin.math.*

private data class Data(
    val latitude: Double,
    val longitude: Double,
    val speed: Float,
    val horizontalAccuracy: Float,
    val speedAccuracy: Float,
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
    private val _maxPredictionHorizontalAccuracyMeters = 20f
    private val _maxPredictionSpeedAccuracyMetersPerSecond = 1.5f
    private val _maxPredictionBearingAccuracyDegrees = 45f
    private val _maxMeasuredSpeedFromObservedMovementMultiplier = 3.0
    private val _maxMeasuredSpeedFromObservedMovementSlackMetersPerSecond = 1.0
    private var _data: Data? = null

    fun filter(point: TrackPoint): TrackPoint {
        val accuracy2 = point.horizontalAccuracyMeters.toDouble().pow(2)
        val bearingAccuracy2 = point.bearingAccuracyDegrees.toDouble().pow(2)
        return _data?.let { data ->
            val deltaTimeSeconds = ((point.timeMs - data.timeMs).coerceAtLeast(0)).toDouble() / 1000.0
            val measuredSpeed = sanitizeMeasuredSpeed(point, data, deltaTimeSeconds)
            val predictedData =
                if (deltaTimeSeconds > 0.0 && shouldPredictMovement(data, point, measuredSpeed)) {
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
                    data.copy(
                        variance = data.variance + if (deltaTimeSeconds > 0.0) processNoise * deltaTimeSeconds else processNoise
                    )
                }

            // Kalman gain
            val k = predictedData.variance / (predictedData.variance + accuracy2)
            // Update estimate with measurement (simple blend)
            val measuredData = data.copy(
                latitude = predictedData.latitude + k * (point.latitudeDegrees - predictedData.latitude),
                longitude = predictedData.longitude + k * (point.longitudeDegrees - predictedData.longitude),
                speed = measuredSpeed,
                horizontalAccuracy = point.horizontalAccuracyMeters,
                speedAccuracy = point.speedAccuracyMeterPerSecond,
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
                val measuredSpeed =
                    if (point.speedMetersPerSecond < speedThreshold) 0f
                    else point.speedMetersPerSecond
                // First measurement: initialize state
                _data = Data(
                    latitude = point.latitudeDegrees,
                    longitude = point.longitudeDegrees,
                    speed = measuredSpeed,
                    horizontalAccuracy = point.horizontalAccuracyMeters,
                    speedAccuracy = point.speedAccuracyMeterPerSecond,
                    bearing = point.bearingDegrees,
                    timeMs = point.timeMs,
                    variance = accuracy2,
                )
                point.copy(speedMetersPerSecond = measuredSpeed)
            }
    }

    private fun sanitizeMeasuredSpeed(
        point: TrackPoint,
        data: Data,
        deltaTimeSeconds: Double,
    ): Float {
        val measuredSpeed =
            if (point.speedMetersPerSecond < speedThreshold) 0f
            else point.speedMetersPerSecond
        if (measuredSpeed <= speedThreshold || deltaTimeSeconds <= 0.0) {
            return measuredSpeed
        }
        val observedDistanceMeters = approximateDistanceMeters(
            lat1 = data.latitude,
            lon1 = data.longitude,
            lat2 = point.latitudeDegrees,
            lon2 = point.longitudeDegrees,
        )
        val observedSpeedMetersPerSecond = observedDistanceMeters / deltaTimeSeconds
        val maxPlausibleMeasuredSpeed =
            observedSpeedMetersPerSecond * _maxMeasuredSpeedFromObservedMovementMultiplier +
                _maxMeasuredSpeedFromObservedMovementSlackMetersPerSecond
        return if (measuredSpeed <= maxPlausibleMeasuredSpeed) measuredSpeed else 0f
    }

    private fun shouldPredictMovement(
        data: Data,
        point: TrackPoint,
        measuredSpeed: Float,
    ): Boolean =
        data.speed > speedThreshold &&
            measuredSpeed > speedThreshold &&
            data.horizontalAccuracy <= _maxPredictionHorizontalAccuracyMeters &&
            point.horizontalAccuracyMeters <= _maxPredictionHorizontalAccuracyMeters &&
            data.speedAccuracy in 0f.._maxPredictionSpeedAccuracyMetersPerSecond &&
            point.speedAccuracyMeterPerSecond in 0f.._maxPredictionSpeedAccuracyMetersPerSecond &&
            point.bearingAccuracyDegrees in 0f.._maxPredictionBearingAccuracyDegrees

    private fun approximateDistanceMeters(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
    ): Double {
        val earthRadiusMeters = 6371000.0
        val latAvg = Math.toRadians((lat1 + lat2) / 2)
        val x = Math.toRadians(lon2 - lon1) * cos(latAvg)
        val y = Math.toRadians(lat2 - lat1)
        return sqrt(x * x + y * y) * earthRadiusMeters
    }

    fun reset() {
        _data = null
    }
}
