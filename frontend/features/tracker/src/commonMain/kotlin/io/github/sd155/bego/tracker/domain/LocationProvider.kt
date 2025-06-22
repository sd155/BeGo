package io.github.sd155.bego.tracker.domain

import io.github.sd155.bego.result.Result

/**
 * Abstract base class for platform-specific location providers.
 * Implementations should provide location updates as TrackPoint objects.
 */
abstract class LocationProvider {
    internal abstract suspend fun start(): Result<LocationError, Unit>
    internal abstract suspend fun sub(onUpdate: (TrackPoint) -> Unit): Result<LocationError, Unit>
    internal abstract fun unsub()
}