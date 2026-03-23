package io.github.sd155.bego.tracker.app

import androidx.compose.runtime.Composable
import io.github.sd155.bego.tracker.domain.LocationError
import io.github.sd155.bego.utils.Result

internal interface TrackerPrerequisites {
    suspend fun ensureReady(): Result<LocationError, Unit>
}

@Composable
internal expect fun rememberTrackerPrerequisites(): TrackerPrerequisites