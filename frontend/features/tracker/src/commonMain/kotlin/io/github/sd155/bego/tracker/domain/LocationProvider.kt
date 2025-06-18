package io.github.sd155.bego.tracker.domain

import io.github.sd155.bego.result.Result

abstract class LocationProvider {
    internal abstract suspend fun start(): Result<LocationError, Unit>
    internal abstract suspend fun sub(onUpdate: (TrackPoint) -> Unit): Result<LocationError, Unit>
    internal abstract fun unsub()
}