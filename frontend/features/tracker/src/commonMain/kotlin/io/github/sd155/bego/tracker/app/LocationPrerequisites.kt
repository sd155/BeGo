package io.github.sd155.bego.tracker.app

import io.github.sd155.bego.tracker.domain.LocationError
import io.github.sd155.bego.utils.Result

/**
 * Platform-specific prerequisites required before location tracking can start.
 *
 * Implementations typically coordinate runtime permissions and system location settings.
 */
abstract class LocationPrerequisites {
    internal abstract suspend fun ensureReady(): Result<LocationError, Unit>
}
