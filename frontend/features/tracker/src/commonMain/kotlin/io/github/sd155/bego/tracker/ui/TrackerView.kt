package io.github.sd155.bego.tracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bego.features.tracker.generated.resources.Res
import bego.features.tracker.generated.resources.distance_template
import bego.features.tracker.generated.resources.finish_template
import bego.features.tracker.generated.resources.pace_template
import bego.features.tracker.generated.resources.reset_action
import bego.features.tracker.generated.resources.speed_template
import bego.features.tracker.generated.resources.start_action
import bego.features.tracker.generated.resources.stop_action
import io.github.sd155.bego.theme.BegoAccentFilledButton
import io.github.sd155.bego.theme.BegoBodyLargeText
import io.github.sd155.bego.theme.BegoDropDown
import io.github.sd155.bego.theme.BegoDropDownItemData
import io.github.sd155.bego.theme.BegoHeaderText
import io.github.sd155.bego.theme.BegoPrimaryFilledButton
import io.github.sd155.bego.theme.BegoTheme
import io.github.sd155.bego.theme.BegoWarningFilledButton
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TrackerView(
    state: TrackerViewState,
    onStart: () -> Unit = {},
    onStop: () -> Unit = {},
    onReset: () -> Unit = {},
    onSetTarget: (Int) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BegoTheme.palette.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        when (state) {
            is TrackerViewState.Initial -> {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    BegoDropDown(
                        dropdownItems = state.targets.map { target ->
                            BegoDropDownItemData(
                                id = target,
                                text = stringResource(Res.string.finish_template, target)
                            )
                        },
                        selectedItemText = stringResource(Res.string.finish_template, state.selectedTarget),
                        onItemSelected = { item -> onSetTarget(item.id) },
                    )
                    BegoHeaderText(text = state.time)
                }
                BegoAccentFilledButton(
                    onClick = onStart,
                    label = stringResource(Res.string.start_action),
                )
                Spacer(modifier = Modifier.height(BegoTheme.sizes.contentVerticalPadding))
            }
            is TrackerViewState.Finished -> {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    BegoBodyLargeText(text = stringResource(Res.string.distance_template, state.distance))
                    BegoHeaderText(text = state.time)
                    BegoBodyLargeText(text = stringResource(Res.string.pace_template, state.pace))
                    BegoBodyLargeText(text = stringResource(Res.string.speed_template, state.speed))
                }
                BegoPrimaryFilledButton(
                    onClick = onReset,
                    label = stringResource(Res.string.reset_action),
                )
                Spacer(modifier = Modifier.height(BegoTheme.sizes.contentVerticalPadding))
            }
            is TrackerViewState.Running -> {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    BegoBodyLargeText(text = stringResource(Res.string.finish_template, state.target))
                    BegoBodyLargeText(text = stringResource(Res.string.distance_template, state.distance))
                    BegoHeaderText(text = state.time)
                    BegoBodyLargeText(text = stringResource(Res.string.pace_template, state.pace))
                    BegoBodyLargeText(text = stringResource(Res.string.speed_template, state.speed))
                }
                BegoWarningFilledButton(
                    onClick = onStop,
                    label = stringResource(Res.string.stop_action),
                )
                Spacer(modifier = Modifier.height(BegoTheme.sizes.contentVerticalPadding))
            }
        }
    }
}
