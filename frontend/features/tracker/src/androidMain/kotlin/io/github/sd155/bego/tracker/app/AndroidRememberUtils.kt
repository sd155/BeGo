package io.github.sd155.bego.tracker.app

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.AndroidTrackerPrerequisites
import io.github.sd155.logs.api.Logger

@Composable
internal actual fun rememberTrackerPrerequisites(): TrackerPrerequisites {
    val activity = requireNotNull(LocalActivity.current as? ComponentActivity) {
        "Tracker prerequisites require ComponentActivity"
    }
    val logger = remember { Inject.instance<Logger>(tag = trackerModuleName) }
    return remember(activity) {
        AndroidTrackerPrerequisites(
            activity = activity,
            logger = logger,
        )
    }
}