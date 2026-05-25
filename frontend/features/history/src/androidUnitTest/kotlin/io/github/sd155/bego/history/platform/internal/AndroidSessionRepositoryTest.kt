package io.github.sd155.bego.history.platform.internal

import android.content.Context
import android.content.ContextWrapper
import io.github.sd155.bego.tracker.api.RunSessionPoint
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class AndroidSessionRepositoryTest {
    @Test
    fun saveSinglePointCreatesSessionFileTest() = runBlocking {
        val filesDir = createFilesDir("single")
        val repository = AndroidSessionRepository(
            applicationContext = TestContext(filesDir),
            logger = TestLogger(),
        )
        val point = sessionPoint()

        val result = repository.save(point)
        val sessionFile = File(filesDir, "history/sessions/${point.sessionStartTimeMs}.jsonl")

        assertIs<io.github.sd155.bego.utils.Result.Success<Unit>>(result)
        assertTrue(sessionFile.exists())
        assertEquals(listOf(point.asJson()), sessionFile.readLines())
    }

    @Test
    fun saveSeveralPointsAppendsToSessionFileTest() = runBlocking {
        val filesDir = createFilesDir("append")
        val repository = AndroidSessionRepository(
            applicationContext = TestContext(filesDir),
            logger = TestLogger(),
        )
        val first = sessionPoint(sessionDurationMs = 0L, sessionDistanceMeters = 0.0)
        val second = sessionPoint(sessionDurationMs = 1000L, sessionDistanceMeters = 12.5)
        val sessionFile = File(filesDir, "history/sessions/${first.sessionStartTimeMs}.jsonl")

        repository.save(first)
        repository.save(second)

        assertEquals(listOf(first.asJson(), second.asJson()), sessionFile.readLines())
    }

    @Test
    fun savePointsFromDifferentSessionsUsesDifferentFilesTest() = runBlocking {
        val filesDir = createFilesDir("split")
        val repository = AndroidSessionRepository(
            applicationContext = TestContext(filesDir),
            logger = TestLogger(),
        )
        val first = sessionPoint(sessionStartTimeMs = 1L)
        val second = sessionPoint(sessionStartTimeMs = 2L)

        repository.save(first)
        repository.save(second)

        val sessionsDir = File(filesDir, "history/sessions")
        val fileNames = sessionsDir.list()?.sorted().orEmpty()
        assertEquals(listOf("1.jsonl", "2.jsonl"), fileNames)
    }

    private fun createFilesDir(name: String): File =
        File(System.getProperty("java.io.tmpdir"), "history-repo-test-$name-${System.nanoTime()}").apply {
            mkdirs()
        }

    private fun sessionPoint(
        sessionStartTimeMs: Long = 123L,
        sessionDurationMs: Long = 0L,
        targetDistanceMeters: Double = 1000.0,
        sessionDistanceMeters: Double = 0.0,
        latitudeDegrees: Double = 60.1699,
        longitudeDegrees: Double = 24.9384,
        altitudeMeters: Double = 0.0,
        bearingDegrees: Float = 0f,
        speedMetersPerSecond: Float = 3f,
        averageSpeedKph: Float = 0f,
        averagePaceMsPerKm: Long = 0L,
    ): RunSessionPoint =
        RunSessionPoint(
            sessionStartTimeMs = sessionStartTimeMs,
            sessionDurationMs = sessionDurationMs,
            targetDistanceMeters = targetDistanceMeters,
            sessionDistanceMeters = sessionDistanceMeters,
            latitudeDegrees = latitudeDegrees,
            longitudeDegrees = longitudeDegrees,
            altitudeMeters = altitudeMeters,
            bearingDegrees = bearingDegrees,
            speedMetersPerSecond = speedMetersPerSecond,
            averageSpeedKph = averageSpeedKph,
            averagePaceMsPerKm = averagePaceMsPerKm,
        )

    private fun RunSessionPoint.asJson(): String =
        "{\"${AndroidSessionRepository.SESSION_START_TIME_PARAM_NAME}\":$sessionStartTimeMs," +
            "\"${AndroidSessionRepository.SESSION_DURATION_PARAM_NAME}\":$sessionDurationMs," +
            "\"${AndroidSessionRepository.TARGET_DISTANCE_PARAM_NAME}\":$targetDistanceMeters," +
            "\"${AndroidSessionRepository.SESSION_DISTANCE_PARAM_NAME}\":$sessionDistanceMeters," +
            "\"${AndroidSessionRepository.LATITUDE_PARAM_NAME}\":$latitudeDegrees," +
            "\"${AndroidSessionRepository.LONGITUDE_PARAM_NAME}\":$longitudeDegrees," +
            "\"${AndroidSessionRepository.ALTITUDE_PARAM_NAME}\":$altitudeMeters," +
            "\"${AndroidSessionRepository.BEARING_PARAM_NAME}\":$bearingDegrees," +
            "\"${AndroidSessionRepository.SPEED_PARAM_NAME}\":$speedMetersPerSecond," +
            "\"${AndroidSessionRepository.AVERAGE_SPEED_PARAM_NAME}\":$averageSpeedKph," +
            "\"${AndroidSessionRepository.AVERAGE_PACE_PARAM_NAME}\":$averagePaceMsPerKm}"

    private class TestContext(
        private val testFilesDir: File,
    ) : ContextWrapper(null) {
        override fun getFilesDir(): File = testFilesDir
        override fun getApplicationContext(): Context = this
    }

    private class TestLogger : Logger {
        override fun trace(event: String, diagnostics: List<Any>) = Unit
        override fun debug(event: String, diagnostics: List<Any>) = Unit
        override fun info(event: String, diagnostics: List<Any>) = Unit
        override fun warn(event: String, e: Throwable?, diagnostics: List<Any>) = Unit
        override fun error(event: String, e: Throwable?, diagnostics: List<Any>) = Unit
        override fun fatal(event: String, e: Throwable?, diagnostics: List<Any>) = Unit
    }
}
