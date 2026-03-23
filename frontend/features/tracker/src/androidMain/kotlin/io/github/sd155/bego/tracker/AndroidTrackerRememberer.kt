package io.github.sd155.bego.tracker

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.app.LocationPrerequisites
import io.github.sd155.bego.tracker.app.PlatformTrackerRememberer
import io.github.sd155.bego.tracker.app.trackerModuleName
import io.github.sd155.logs.api.Logger

class AndroidTrackerRememberer : PlatformTrackerRememberer() {

    @Composable
    override fun rememberLocationPrerequisites(): LocationPrerequisites {
        val activity = requireNotNull(LocalActivity.current as? ComponentActivity) {
            "Tracker prerequisites require ComponentActivity"
        }
        val logger = remember { Inject.instance<Logger>(tag = trackerModuleName) }
        return remember(activity) {
            AndroidLocationPrerequisites(
                activity = activity,
                logger = logger,
            )
        }
    }
}
