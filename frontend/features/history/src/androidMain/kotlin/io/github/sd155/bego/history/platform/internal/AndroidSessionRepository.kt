package io.github.sd155.bego.history.platform.internal

import android.content.Context
import io.github.sd155.bego.history.app.SessionRepository
import io.github.sd155.bego.history.app.SessionRepositoryFailure
import io.github.sd155.bego.tracker.api.RunSessionPoint
import io.github.sd155.bego.utils.Result
import io.github.sd155.bego.utils.asFailure
import io.github.sd155.bego.utils.asSuccess
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File

/**
 * Stores session points as append-only JSONL files in app-private storage.
 *
 * Each run session gets its own file under `history/sessions/<sessionStartTimeMs>.jsonl`.
 */
internal class AndroidSessionRepository(
    applicationContext: Context,
    private val logger: Logger,
) : SessionRepository() {
    private val _scope = CoroutineScope(SupervisorJob() + Dispatchers.IO.limitedParallelism(1))
    private val _filesDir = applicationContext.applicationContext.filesDir
    private val _sessionsDir = File(_filesDir, SESSIONS_DIR_PATH)
    private val _fileLock = Any()

    override fun consume(sessionPoint: RunSessionPoint) {
        _scope.launch { persist(sessionPoint) }
    }

    private fun persist(sessionPoint: RunSessionPoint): Result<SessionRepositoryFailure, Unit> {
        return try {
            synchronized(_fileLock) {
                if (!_sessionsDir.exists() && !_sessionsDir.mkdirs()) {
                    throw IllegalStateException("Failed to create sessions directory: ${_sessionsDir.absolutePath}")
                }
                val sessionFile = sessionFile(sessionPoint.sessionStartTimeMs)
                sessionFile.appendText(sessionPoint.toJsonLine())
            }
            Unit.asSuccess()
        }
        catch (e: Exception) {
            logger.error(event = "Failed to persist session point", e = e)
            SessionRepositoryFailure.asFailure()
        }
    }

    private fun sessionFile(sessionStartTimeMs: Long): File =
        File(_sessionsDir, "$sessionStartTimeMs.jsonl")

    private fun RunSessionPoint.toJsonLine(): String =
        buildString {
            append('{')
            append("\"$SESSION_START_TIME_PARAM_NAME\":").append(sessionStartTimeMs).append(',')
            append("\"$SESSION_DURATION_PARAM_NAME\":").append(sessionDurationMs).append(',')
            append("\"$TARGET_DISTANCE_PARAM_NAME\":").append(targetDistanceMeters).append(',')
            append("\"$SESSION_DISTANCE_PARAM_NAME\":").append(sessionDistanceMeters).append(',')
            append("\"$LATITUDE_PARAM_NAME\":").append(latitudeDegrees).append(',')
            append("\"$LONGITUDE_PARAM_NAME\":").append(longitudeDegrees).append(',')
            append("\"$ALTITUDE_PARAM_NAME\":").append(altitudeMeters).append(',')
            append("\"$BEARING_PARAM_NAME\":").append(bearingDegrees).append(',')
            append("\"$SPEED_PARAM_NAME\":").append(speedMetersPerSecond).append(',')
            append("\"$AVERAGE_SPEED_PARAM_NAME\":").append(averageSpeedKph).append(',')
            append("\"$AVERAGE_PACE_PARAM_NAME\":").append(averagePaceMsPerKm)
            append("}\n")
        }

    internal companion object {
        private const val SESSIONS_DIR_PATH = "history/sessions"

        const val ALTITUDE_PARAM_NAME = "altitude_meters"
        const val AVERAGE_PACE_PARAM_NAME = "average_pace_ms_per_km"
        const val AVERAGE_SPEED_PARAM_NAME = "average_speed_kph"
        const val BEARING_PARAM_NAME = "bearing_degrees"
        const val TARGET_DISTANCE_PARAM_NAME = "target_distance_meters"
        const val LATITUDE_PARAM_NAME = "latitude_degrees"
        const val LONGITUDE_PARAM_NAME = "longitude_degrees"
        const val SESSION_DISTANCE_PARAM_NAME = "session_distance_meters"
        const val SESSION_DURATION_PARAM_NAME = "session_duration_ms"
        const val SESSION_START_TIME_PARAM_NAME = "session_start_time_ms"
        const val SPEED_PARAM_NAME = "speed_meters_per_second"
    }
}
