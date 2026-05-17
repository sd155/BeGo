package io.github.sd155.bego.tracker.domain

import io.github.sd155.bego.tracker.api.RunSessionPoint
import io.github.sd155.bego.tracker.app.LocationProvider
import io.github.sd155.bego.utils.Result
import io.github.sd155.bego.utils.asFailure
import io.github.sd155.bego.utils.asSuccess
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TrackerTest {
    @Test
    fun setTargetDistanceTest() {
        val tracker = Tracker(
            logger = TestLogger(),
            sessionWriter = {},
            locationProvider = TestLocationProvider(),
        )

        tracker.setTargetDistance(5000.0)

        assertEquals(5000.0, tracker.state.value.finish)
    }

    @Test
    fun startTrackerLocationFailureTest() = runBlocking {
        val tracker = Tracker(
            logger = TestLogger(),
            sessionWriter = {},
            locationProvider = TestLocationProvider(
                subResult = LocationError.IllegalState.asFailure(),
            ),
        )

        val result = tracker.start()

        assertIs<Result.Failure<LocationError>>(result)
        assertEquals(LocationError.IllegalState, result.error)
    }

    @Test
    fun resetTrackerTest() = runBlocking {
        val locationProvider = TestLocationProvider()
        val tracker = Tracker(
            logger = TestLogger(),
            sessionWriter = {},
            locationProvider = locationProvider,
        )

        tracker.setTargetDistance(1000.0)
        tracker.start()
        awaitState(tracker) { it.isRunning() }

        locationProvider.emit(point(timeMs = 1_000L, latitude = 60.16990, longitude = 24.9384))
        locationProvider.emit(point(timeMs = 2_000L, latitude = 60.16992, longitude = 24.9384))
        locationProvider.emit(point(timeMs = 3_000L, latitude = 60.16994, longitude = 24.9384))
        awaitState(tracker) { it.distance > 0.0 }

        tracker.reset()
        awaitState(tracker) { it.startTime == 0L && it.distance == 0.0 && it.finish == 0.0 && it.last == null }

        assertEquals(1, locationProvider.unsubCalls)
        assertNull(tracker.state.value.last)
    }

    @Test
    fun setTargetDistanceWhileRunningTest() = runBlocking {
        val tracker = Tracker(
            logger = TestLogger(),
            sessionWriter = {},
            locationProvider = TestLocationProvider(),
        )

        tracker.setTargetDistance(1000.0)
        tracker.start()
        awaitState(tracker) { it.isRunning() }

        tracker.setTargetDistance(5000.0)

        assertEquals(1000.0, tracker.state.value.finish)
        tracker.stop()
    }

    @Test
    fun rejectedPointStillAdvancesLastPointTest() = runBlocking {
        val locationProvider = TestLocationProvider()
        val tracker = Tracker(
            logger = TestLogger(),
            sessionWriter = {},
            locationProvider = locationProvider,
        )

        tracker.start()
        awaitState(tracker) { it.isRunning() }

        locationProvider.emit(point(timeMs = 1_000L, latitude = 60.1699, longitude = 24.9384, speedMetersPerSecond = 0f))
        locationProvider.emit(point(timeMs = 2_000L, latitude = 60.1699, longitude = 24.9384, speedMetersPerSecond = 0f))

        awaitState(tracker) { it.last?.timeMs == 2_000L }

        assertEquals(0.0, tracker.state.value.distance)
        tracker.stop()
    }

    @Test
    fun stationarySpeedSpikeDoesNotAccumulateDistanceTest() = runBlocking {
        val locationProvider = TestLocationProvider()
        val tracker = Tracker(
            logger = TestLogger(),
            sessionWriter = {},
            locationProvider = locationProvider,
        )

        tracker.start()
        awaitState(tracker) { it.isRunning() }

        locationProvider.emit(point(timeMs = 1_000L, latitude = 60.1699, longitude = 24.9384, speedMetersPerSecond = 0f, horizontalAccuracyMeters = 11.651f))
        locationProvider.emit(point(timeMs = 2_000L, latitude = 60.1699, longitude = 24.9384, speedMetersPerSecond = 0f, horizontalAccuracyMeters = 11.651f))
        locationProvider.emit(point(timeMs = 3_000L, latitude = 60.1699, longitude = 24.9384, speedMetersPerSecond = 5.61f, horizontalAccuracyMeters = 11.651f))
        locationProvider.emit(point(timeMs = 9_000L, latitude = 60.1699, longitude = 24.9384, speedMetersPerSecond = 0.189f, horizontalAccuracyMeters = 11.651f))
        locationProvider.emit(point(timeMs = 15_000L, latitude = 60.1699, longitude = 24.9384, speedMetersPerSecond = 0f, horizontalAccuracyMeters = 11.651f))

        delay(50)

        assertEquals(0.0, tracker.state.value.distance)
        tracker.stop()
    }

    @Test
    fun tinyDriftWithLargeSpeedSpikeDoesNotAccumulateDistanceTest() = runBlocking {
        val locationProvider = TestLocationProvider()
        val tracker = Tracker(
            logger = TestLogger(),
            sessionWriter = {},
            locationProvider = locationProvider,
        )

        tracker.start()
        awaitState(tracker) { it.isRunning() }

        locationProvider.emit(point(timeMs = 1_000L, latitude = 60.1699000, longitude = 24.9384000, speedMetersPerSecond = 0f))
        locationProvider.emit(point(timeMs = 2_000L, latitude = 60.1699004, longitude = 24.9384000, speedMetersPerSecond = 8f))
        locationProvider.emit(point(timeMs = 3_000L, latitude = 60.1699008, longitude = 24.9384000, speedMetersPerSecond = 9f))
        locationProvider.emit(point(timeMs = 4_000L, latitude = 60.1699012, longitude = 24.9384000, speedMetersPerSecond = 8.5f))

        delay(50)

        assertEquals(0.0, tracker.state.value.distance)
        tracker.stop()
    }

    @Test
    fun acceptedPointsWriteRunSessionPointsTest() = runBlocking {
        val locationProvider = TestLocationProvider()
        val sessionPoints = mutableListOf<RunSessionPoint>()
        val tracker = Tracker(
            logger = TestLogger(),
            sessionWriter = { point -> sessionPoints += point },
            locationProvider = locationProvider,
        )

        tracker.setTargetDistance(1000.0)
        tracker.start()
        awaitState(tracker) { it.isRunning() }

        locationProvider.emit(point(timeMs = 1_000L, latitude = 60.16990, longitude = 24.9384))
        locationProvider.emit(point(timeMs = 2_000L, latitude = 60.16992, longitude = 24.9384))
        locationProvider.emit(point(timeMs = 3_000L, latitude = 60.16994, longitude = 24.9384))
        awaitState(tracker) { it.distance > 0.0 }

        assertEquals(3, sessionPoints.size)
        val startTime = sessionPoints.first().sessionStartTimeMs
        assertTrue(startTime > 0L)
        sessionPoints.forEach { assertEquals(startTime, it.sessionStartTimeMs) }
        assertEquals(tracker.state.value.startTime, startTime)
        assertEquals(0.0, sessionPoints.first().sessionDistanceMeters)
        assertEquals(0L, sessionPoints.first().averagePaceMsPerKm)
        assertEquals(0f, sessionPoints.first().averageSpeedKph)
        assertEquals(tracker.state.value.finish, sessionPoints.last().targetDistanceMeters)
        assertEquals(tracker.state.value.time, sessionPoints.last().sessionDurationMs)
        assertEquals(tracker.state.value.distance, sessionPoints.last().sessionDistanceMeters)
        assertEquals(tracker.state.value.speed, sessionPoints.last().averageSpeedKph)
        assertEquals(tracker.state.value.pace, sessionPoints.last().averagePaceMsPerKm)
    }

    private suspend fun awaitState(
        tracker: Tracker,
        predicate: (TrackerState) -> Boolean,
    ) {
        repeat(100) {
            if (predicate(tracker.state.value)) return
            delay(10)
        }
        assertTrue(predicate(tracker.state.value), "Timed out waiting for tracker state: ${tracker.state.value}")
    }

    private fun point(
        timeMs: Long = 0L,
        latitude: Double,
        longitude: Double,
        horizontalAccuracyMeters: Float = 1f,
        speedMetersPerSecond: Float = 3f,
    ) = TrackPoint(
        timeMs = timeMs,
        latitudeDegrees = latitude,
        longitudeDegrees = longitude,
        horizontalAccuracyMeters = horizontalAccuracyMeters,
        altitudeMeters = 0.0,
        altitudeAccuracyMeters = 1f,
        speedMetersPerSecond = speedMetersPerSecond,
        speedAccuracyMeterPerSecond = 1f,
        bearingDegrees = 0f,
        bearingAccuracyDegrees = 1f,
    )

    private class TestLocationProvider(
        private val subResult: Result<LocationError, Unit> = Unit.asSuccess(),
    ) : LocationProvider() {
        private var onUpdate: ((TrackPoint) -> Unit)? = null
        var unsubCalls: Int = 0
            private set

        override suspend fun sub(onUpdate: (TrackPoint) -> Unit): Result<LocationError, Unit> {
            this.onUpdate = onUpdate
            return subResult
        }

        override fun unsub() {
            unsubCalls += 1
            onUpdate = null
        }

        fun emit(point: TrackPoint) {
            onUpdate?.invoke(point)
        }
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
