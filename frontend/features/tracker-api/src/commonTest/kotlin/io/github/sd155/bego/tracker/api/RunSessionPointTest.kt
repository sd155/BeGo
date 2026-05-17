package io.github.sd155.bego.tracker.api

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RunSessionPointTest {
    @Test
    fun validRunSessionPointTest() {
        val point = buildRunSessionPoint()

        assertEquals(1L, point.sessionStartTimeMs)
        assertEquals(0L, point.sessionDurationMs)
        assertEquals(60.1699, point.latitudeDegrees)
        assertEquals(24.9384, point.longitudeDegrees)
    }

    @Test
    fun boundaryRunSessionPointTest() {
        val point = buildRunSessionPoint(
            latitudeDegrees = -90.0,
            longitudeDegrees = 180.0,
            bearingDegrees = 360f,
        )

        assertEquals(-90.0, point.latitudeDegrees)
        assertEquals(180.0, point.longitudeDegrees)
        assertEquals(360f, point.bearingDegrees)
    }

    @Test
    fun invalidRunSessionPointTest() {
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(sessionStartTimeMs = 0L)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(sessionDurationMs = -1L)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(targetDistanceMeters = -1.0)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(sessionDistanceMeters = -1.0)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(latitudeDegrees = -91.0)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(latitudeDegrees = 91.0)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(longitudeDegrees = -181.0)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(longitudeDegrees = 181.0)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(bearingDegrees = -1f)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(bearingDegrees = 361f)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(speedMetersPerSecond = -1f)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(averageSpeedKph = -1f)
        }
        assertFailsWith<IllegalArgumentException> {
            buildRunSessionPoint(averagePaceMsPerKm = -1L)
        }
    }

    private fun buildRunSessionPoint(
        sessionStartTimeMs: Long = 1L,
        sessionDurationMs: Long = 0L,
        targetDistanceMeters: Double = 1000.0,
        sessionDistanceMeters: Double = 0.0,
        latitudeDegrees: Double = 60.1699,
        longitudeDegrees: Double = 24.9384,
        altitudeMeters: Double = 0.0,
        bearingDegrees: Float = 0f,
        speedMetersPerSecond: Float = 0f,
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
}
