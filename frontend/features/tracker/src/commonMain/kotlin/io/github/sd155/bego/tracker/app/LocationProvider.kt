package io.github.sd155.bego.tracker.app

import io.github.sd155.bego.tracker.domain.LocationError
import io.github.sd155.bego.tracker.domain.TrackPoint
import io.github.sd155.bego.utils.Result

/**
 * Abstract base class for platform-specific location providers.
 * Implementations should provide location updates as TrackPoint objects.
 */
abstract class LocationProvider {
    internal abstract suspend fun sub(onUpdate: (TrackPoint) -> Unit): Result<LocationError, Unit>
    internal abstract fun unsub()
}