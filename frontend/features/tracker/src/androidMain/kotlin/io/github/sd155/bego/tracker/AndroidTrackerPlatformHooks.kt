package io.github.sd155.bego.tracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.res.stringResource
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.app.LocationPrerequisites
import io.github.sd155.bego.tracker.app.TrackerPlatformHooks
import io.github.sd155.bego.tracker.app.trackerModuleName
import io.github.sd155.bego.theme.BegoBodyLargeText
import io.github.sd155.bego.theme.BegoPrimaryFilledButton
import io.github.sd155.bego.theme.BegoTheme
import io.github.sd155.bego.tracker.domain.PlatformReason
import io.github.sd155.logs.api.Logger

class AndroidTrackerPlatformHooks : TrackerPlatformHooks() {

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

    @Composable
    override fun PlatformNotReadyView(
        reason: PlatformReason,
        onRetryInitialization: () -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                BegoBodyLargeText(
                    text = stringResource(
                        when (reason) {
                            PlatformReason.Permissions -> R.string.permissions_denied_hint
                            PlatformReason.Settings -> R.string.settings_denied_hint
                        }
                    )
                )
            }
            BegoPrimaryFilledButton(
                onClick = onRetryInitialization,
                label = stringResource(R.string.retry_action),
            )
            Spacer(modifier = Modifier.height(BegoTheme.sizes.contentVerticalPadding))
        }
    }
}
