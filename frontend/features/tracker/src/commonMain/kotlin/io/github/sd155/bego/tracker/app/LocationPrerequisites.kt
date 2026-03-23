package io.github.sd155.bego.tracker.app

import io.github.sd155.bego.tracker.domain.LocationError
import io.github.sd155.bego.utils.Result

abstract class LocationPrerequisites {
    internal abstract suspend fun ensureReady(): Result<LocationError, Unit>
}