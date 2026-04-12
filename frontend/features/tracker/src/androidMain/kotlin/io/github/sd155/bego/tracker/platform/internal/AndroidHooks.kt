package io.github.sd155.bego.tracker.platform.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.sd155.bego.theme.BegoBodyLargeText
import io.github.sd155.bego.theme.BegoPrimaryFilledButton
import io.github.sd155.bego.theme.BegoTheme
import io.github.sd155.bego.tracker.R
import io.github.sd155.bego.tracker.app.LocationPrerequisites
import io.github.sd155.bego.tracker.app.PlatformHooks
import io.github.sd155.bego.tracker.domain.PlatformReason
import io.github.sd155.bego.tracker.platform.utils.AndroidPermissionValidator
import io.github.sd155.logs.api.Logger

internal class AndroidHooks(
    private val logger: Logger,
) : PlatformHooks() {

    @Composable
    override fun rememberLocationPrerequisites(): LocationPrerequisites {
        val activity = requireNotNull(LocalActivity.current as? ComponentActivity) {
            "Tracker prerequisites require ComponentActivity"
        }
        val permissionCallback = remember {
            PermissionResultCallback(logger = logger)
        }
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = permissionCallback::onResult,
        )
        val settingsCallback = remember {
            SettingsResolutionResultCallback(logger = logger)
        }
        val settingsLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = settingsCallback::onResult,
        )

        return remember(activity, logger, permissionLauncher, settingsLauncher) {
            val permissionValidator = AndroidPermissionValidator(
                activityResultLauncher = permissionLauncher,
                logger = logger,
            )
            AndroidLocationPrerequisites(
                activity = activity,
                logger = logger,
                permissions = permissionValidator,
                settingsLauncher = settingsLauncher,
            ).also { prerequisites ->
                permissionCallback.bind(permissionValidator)
                settingsCallback.bind(prerequisites)
            }
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

private class PermissionResultCallback(
    private val logger: Logger,
) {
    private var validator: AndroidPermissionValidator? = null

    fun bind(value: AndroidPermissionValidator) {
        validator = value
    }

    fun onResult(results: Map<String, Boolean>) {
        val currentValidator = validator
        if (currentValidator == null) {
            logger.warn(event = "Received permission result before validator initialization")
            return
        }
        currentValidator.onActivityCallback(results)
    }
}

private class SettingsResolutionResultCallback(
    private val logger: Logger,
) {
    private var prerequisites: AndroidLocationPrerequisites? = null

    fun bind(value: AndroidLocationPrerequisites) {
        prerequisites = value
    }

    fun onResult(result: ActivityResult) {
        val currentPrerequisites = prerequisites
        if (currentPrerequisites == null) {
            logger.warn(event = "Received settings resolution result before prerequisites initialization")
            return
        }
        currentPrerequisites.onSettingsResolutionResult(result.resultCode)
    }
}
