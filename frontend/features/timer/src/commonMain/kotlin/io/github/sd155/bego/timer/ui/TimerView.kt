package io.github.sd155.bego.timer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bego.features.timer.generated.resources.Res
import bego.features.timer.generated.resources.continue_action
import bego.features.timer.generated.resources.start_action
import bego.features.timer.generated.resources.stop_action
import bego.features.timer.generated.resources.next_lap_action
import bego.features.timer.generated.resources.reset_action
import io.github.sd155.bego.theme.BegoBodyLargeText
import io.github.sd155.bego.theme.BegoAccentFilledButton
import io.github.sd155.bego.theme.BegoHeaderText
import io.github.sd155.bego.theme.BegoPrimaryFilledButton
import io.github.sd155.bego.theme.BegoTheme
import io.github.sd155.bego.theme.BegoWarningFilledButton
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TimerView(
    state: TimerViewState,
    onStart: () -> Unit = {},
    onStop: () -> Unit = {},
    onContinue: () -> Unit = {},
    onReset: () -> Unit = {},
    onNextLap: () -> Unit = {},
) {
    var totalTime = 0L
    var lapTime = 0L
    var showLapTime = false
    var startAction: (() -> Unit)? = null
    var stopAction: (() -> Unit)? = null
    var nextAction: (() -> Unit)? = null
    var resetAction: (() -> Unit)? = null
    var continueAction: (() -> Unit)? = null
    when (state) {
        TimerViewState.Initial -> {
            startAction = onStart
        }
        is TimerViewState.RunningNoLaps -> {
            totalTime = state.totalTimeCs
            stopAction = onStop
            nextAction = onNextLap
        }
        is TimerViewState.StoppedNoLaps -> {
            totalTime = state.totalTimeCs
            continueAction = onContinue
            resetAction = onReset
        }
        is TimerViewState.RunningWithLaps -> {
            totalTime = state.totalTimeCs
            lapTime = state.currentLapTimeCs
            showLapTime = true
            stopAction = onStop
            nextAction = onNextLap
        }
        is TimerViewState.StoppedWithLaps -> {
            totalTime = state.totalTimeCs
            lapTime = state.currentLapTimeCs
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
            totalTime = formatTime(totalTime),
            lapTime = if (showLapTime) formatTime(lapTime) else null,
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

private fun formatTime(timeInCentiSeconds: Long = 0L): String {
    val totalSeconds = timeInCentiSeconds / 100
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val centiSeconds = timeInCentiSeconds % 100

    return "%02d:%02d.%02d".format(minutes, seconds, centiSeconds)
}