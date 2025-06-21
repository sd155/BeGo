package io.github.sd155.bego.tracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bego.features.tracker.generated.resources.Res
import bego.features.tracker.generated.resources.reset_action
import bego.features.tracker.generated.resources.start_action
import bego.features.tracker.generated.resources.stop_action
import io.github.sd155.bego.theme.BegoAccentFilledButton
import io.github.sd155.bego.theme.BegoBodyLargeText
import io.github.sd155.bego.theme.BegoHeaderText
import io.github.sd155.bego.theme.BegoPrimaryFilledButton
import io.github.sd155.bego.theme.BegoTheme
import io.github.sd155.bego.theme.BegoWarningFilledButton
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TrackerView(
    state: TrackerViewState,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BegoTheme.palette.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BegoHeaderText(text = state.time)
            BegoBodyLargeText(text = state.distance)
        }
        TrackerAction(
            status = state.status,
            onStart = onStart,
            onStop = onStop,
            onReset = onReset,
        )
        Spacer(modifier = Modifier.height(BegoTheme.sizes.contentVerticalPadding))
    }
}

@Composable
private fun TrackerAction(
    status: TrackerStatus,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
) {
    when (status) {
        TrackerStatus.Initial ->
            BegoAccentFilledButton(
                onClick = onStart,
                label = stringResource(Res.string.start_action),
            )
        TrackerStatus.Running ->
            BegoWarningFilledButton(
                onClick = onStop,
                label = stringResource(Res.string.stop_action),
            )
        TrackerStatus.Finished ->
            BegoPrimaryFilledButton(
                onClick = onReset,
                label = stringResource(Res.string.reset_action),
            )
    }
}
