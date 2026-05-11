package io.github.sd155.bego.history.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FinishedRun(
    @SerialName("id")
    val id: String,
    @SerialName("finished_at_epoch_ms")
    val finishedAtEpochMs: Long,
    @SerialName("target_distance_meters")
    val targetDistanceMeters: Double,
    @SerialName("distance_meters")
    val distanceMeters: Double,
    @SerialName("duration_ms")
    val durationMs: Long,
    @SerialName("average_speed_kph")
    val averageSpeedKph: Float,
    @SerialName("average_pace_ms_per_km")
    val averagePaceMsPerKm: Long,
) {
    init {
        require(id.isNotBlank()) { "Finished run id must not be blank" }
        require(finishedAtEpochMs > 0L) { "Finished run time must be greater than zero" }
        require(targetDistanceMeters >= 0.0) { "Target distance must not be negative" }
        require(distanceMeters >= 0.0) { "Distance must not be negative" }
        require(durationMs >= 0L) { "Duration must not be negative" }
        require(averageSpeedKph >= 0f) { "Average speed must not be negative" }
        require(averagePaceMsPerKm >= 0L) { "Average pace must not be negative" }
    }
}