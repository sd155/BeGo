package io.github.sd155.bego.history.test

import io.github.sd155.bego.history.api.FinishedRun
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FinishedRunTest {
    @Test
    fun validFinishedRunTest() {
        val run = buildFinishedRun()

        assertEquals("run-1", run.id)
        assertEquals(1000.0, run.targetDistanceMeters)
        assertEquals(1012.5, run.distanceMeters)
    }

    @Test
    fun invalidFinishRunTest() {
        assertFailsWith<IllegalArgumentException> {
            buildFinishedRun(id = " ")
        }
        assertFailsWith<IllegalArgumentException> {
            buildFinishedRun(finishedAtEpochMs = 0L)
        }
        assertFailsWith<IllegalArgumentException> {
            buildFinishedRun(targetDistanceMeters = -1.0)
        }
        assertFailsWith<IllegalArgumentException> {
            buildFinishedRun(distanceMeters = -1.0)
        }
        assertFailsWith<IllegalArgumentException> {
            buildFinishedRun(durationMs = -1L)
        }
        assertFailsWith<IllegalArgumentException> {
            buildFinishedRun(averageSpeedKph = -1f)
        }
        assertFailsWith<IllegalArgumentException> {
            buildFinishedRun(averagePaceMsPerKm = -1L)
        }
    }

    private fun buildFinishedRun(
        id: String = "run-1",
        finishedAtEpochMs: Long = 1L,
        targetDistanceMeters: Double = 1000.0,
        distanceMeters: Double = 1012.5,
        durationMs: Long = 356_000L,
        averageSpeedKph: Float = 10.2f,
        averagePaceMsPerKm: Long = 352_000L,
    ): FinishedRun =
        FinishedRun(
            id = id,
            finishedAtEpochMs = finishedAtEpochMs,
            targetDistanceMeters = targetDistanceMeters,
            distanceMeters = distanceMeters,
            durationMs = durationMs,
            averageSpeedKph = averageSpeedKph,
            averagePaceMsPerKm = averagePaceMsPerKm,
        )
}
