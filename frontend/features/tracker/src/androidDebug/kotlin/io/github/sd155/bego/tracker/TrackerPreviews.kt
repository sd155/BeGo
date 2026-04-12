package io.github.sd155.bego.tracker

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.sd155.bego.theme.LOCALE_EN
import io.github.sd155.bego.theme.LOCALE_RU
import io.github.sd155.bego.theme.PHONE_PORT_SPEC
import io.github.sd155.bego.theme.ThemedPreview
import io.github.sd155.bego.tracker.platform.internal.AndroidHooks
import io.github.sd155.bego.tracker.domain.PlatformReason
import io.github.sd155.bego.tracker.ui.TrackerView
import io.github.sd155.bego.tracker.ui.TrackerViewState
import io.github.sd155.bego.tracker.ui.UiFormatter
import io.github.sd155.logs.createAndroidLogger

private val formatter = UiFormatter()
private val hooks = AndroidHooks(logger = createAndroidLogger(sourceTag = "PREVIEW"))

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Composable
private fun TrackerCheckingReadinessPreview() {
    ThemedPreview {
        TrackerView(
            state = TrackerViewState.Initialization
        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Composable
private fun TrackerFatalPreview() {
    ThemedPreview {
        TrackerView(
            state = TrackerViewState.FatalInitializationError
        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Composable
private fun TrackerPlatformNotReadyPermissionsPreview() {
    ThemedPreview {
        val reason = PlatformReason.Permissions
        TrackerView(
            state = TrackerViewState.PlatformNotReady(reason),
            notReadyContent = { hooks.PlatformNotReadyView(reason) }
        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Composable
private fun TrackerPlatformNotReadySettingsPreview() {
    ThemedPreview {
        val reason = PlatformReason.Settings
        TrackerView(
            state = TrackerViewState.PlatformNotReady(reason),
            notReadyContent = { hooks.PlatformNotReadyView(reason) }
        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Composable
private fun TrackerInitialPreview() {
    ThemedPreview {
        TrackerView(
            state = TrackerViewState.Initial(
                time = formatter.formatTime(timeMs = 0L),
                targets = listOf(1, 2, 3),
                selectedTarget = 1,
            )
        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Composable
private fun TrackerRunningPreview() {
    ThemedPreview {
        TrackerView(
            state = TrackerViewState.Running(
                time = formatter.formatTime(timeMs = 35665L),
                target = formatter.formatTarget(distanceMeters = 1000.0),
                distance = formatter.formatDistance(distanceMeters = 123.0),
                pace = formatter.formatPace(paceMsPerKm = 1235L),
                speed = formatter.formatSpeed(speedKph = 15.5f),
            ),
        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Composable
private fun TrackerFinishedPreview() {
    ThemedPreview {
        TrackerView(
            state = TrackerViewState.Finished(
                time = formatter.formatTime(timeMs = 155665L),
                distance = formatter.formatDistance(distanceMeters = 1023.5),
                pace = formatter.formatPace(paceMsPerKm = 4567L),
                speed = formatter.formatSpeed(speedKph = 11.5f),
            ),
        )
    }
} 
