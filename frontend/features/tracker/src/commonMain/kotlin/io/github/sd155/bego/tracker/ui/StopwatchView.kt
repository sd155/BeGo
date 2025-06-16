package io.github.sd155.bego.tracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bego.features.tracker.generated.resources.Res
import bego.features.tracker.generated.resources.continue_action
import bego.features.tracker.generated.resources.start_action
import bego.features.tracker.generated.resources.stop_action
import bego.features.tracker.generated.resources.next_lap_action
import bego.features.tracker.generated.resources.reset_action
import io.github.sd155.bego.theme.BegoBodyLargeText
import io.github.sd155.bego.theme.BegoAccentFilledButton
import io.github.sd155.bego.theme.BegoHeaderText
import io.github.sd155.bego.theme.BegoPrimaryFilledButton
import io.github.sd155.bego.theme.BegoTheme
import io.github.sd155.bego.theme.BegoWarningFilledButton
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun StopwatchView(
    state: StopwatchViewState,
    onStart: () -> Unit = {},
    onStop: () -> Unit = {},
    onContinue: () -> Unit = {},
    onReset: () -> Unit = {},
    onNextLap: () -> Unit = {},
) {
    var totalTime = ""
    var lapTime = ""
    var showLapTime = false
    var startAction: (() -> Unit)? = null
    var stopAction: (() -> Unit)? = null
    var nextAction: (() -> Unit)? = null
    var resetAction: (() -> Unit)? = null
    var continueAction: (() -> Unit)? = null
    when (state) {
        is StopwatchViewState.Initial -> {
            startAction = onStart
            totalTime = state.totalTime
        }
        is StopwatchViewState.RunningNoLaps -> {
            totalTime = state.totalTime
            stopAction = onStop
            nextAction = onNextLap
        }
        is StopwatchViewState.StoppedNoLaps -> {
            totalTime = state.totalTime
            continueAction = onContinue
            resetAction = onReset
        }
        is StopwatchViewState.RunningWithLaps -> {
            totalTime = state.totalTime
            lapTime = state.currentLapTime
            showLapTime = true
            stopAction = onStop
            nextAction = onNextLap
        }
        is StopwatchViewState.StoppedWithLaps -> {
            totalTime = state.totalTime
            lapTime = state.currentLapTime
            showLapTime = true
            continueAction = onContinue
            resetAction = onReset
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BegoTheme.palette.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        Timer(
            modifier = Modifier
                .weight(1f),
            totalTime = totalTime,
            lapTime = if (showLapTime) lapTime else null,
        )
        Actions(
            onStart = startAction,
            onStop = stopAction,
            onNext = nextAction,
            onContinue = continueAction,
            onReset = resetAction,
        )
        Spacer(modifier = Modifier.height(BegoTheme.sizes.contentVerticalPadding))
    }
}

@Composable
private fun Timer(
    modifier: Modifier = Modifier,
    totalTime: String,
    lapTime: String?,
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    BegoHeaderText(text = totalTime)
    lapTime?.let { BegoBodyLargeText(text = it) }
}

@Composable
private fun Actions(
    onStart: (() -> Unit)? = null,
    onStop: (() -> Unit)? = null,
    onNext: (() -> Unit)? = null,
    onReset: (() -> Unit)? = null,
    onContinue: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        onStart?.let { onAction ->
            BegoAccentFilledButton(
                onClick = onAction,
                label = stringResource(Res.string.start_action),
            )
        }
        onStop?.let { onAction ->
            BegoWarningFilledButton(
                onClick = onAction,
                label = stringResource(Res.string.stop_action),
            )
        }
        onNext?.let { onAction ->
            BegoPrimaryFilledButton(
                onClick = onAction,
                label = stringResource(Res.string.next_lap_action),
            )
        }
        onContinue?.let { onAction ->
            BegoAccentFilledButton(
                onClick = onAction,
                label = stringResource(Res.string.continue_action),
            )
        }
        onReset?.let { onAction ->
            BegoPrimaryFilledButton(
                onClick = onAction,
                label = stringResource(Res.string.reset_action),
            )
        }
    }
}
