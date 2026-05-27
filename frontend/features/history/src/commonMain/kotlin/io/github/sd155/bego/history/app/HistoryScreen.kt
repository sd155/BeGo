package io.github.sd155.bego.history.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import bego.features.history.generated.resources.Res
import bego.features.history.generated.resources.dock_item_label
import bego.features.history.generated.resources.history_placeholder_body
import bego.features.history.generated.resources.history_placeholder_eyebrow
import bego.features.history.generated.resources.history_placeholder_next_body
import bego.features.history.generated.resources.history_placeholder_next_title
import bego.features.history.generated.resources.history_placeholder_replay_body
import bego.features.history.generated.resources.history_placeholder_replay_title
import bego.features.history.generated.resources.history_placeholder_title
import bego.features.history.generated.resources.history_screen_label
import io.github.sd155.bego.di.DiTree
import io.github.sd155.bego.theme.BegoBodyLargeText
import io.github.sd155.bego.theme.BegoHeaderText
import io.github.sd155.bego.theme.BegoTheme
import io.github.sd155.bego.theme.DockItem
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

/**
 * Navigation route object for the history screen.
 * Used with navigation libraries to identify the history feature destination.
 */
@Serializable
object HistoryScreenRoute

@Composable
fun HistoryDockItem(
    selected: Boolean,
    onClick: () -> Unit,
) =
    DockItem(
        label = stringResource(Res.string.dock_item_label),
        selected = selected,
        onClick = onClick,
    )

/**
 * Main entry point composable for the history feature.
 * Displays the history UI and handles user interaction.
 *
 * This is the feature app-layer composable. It resolves feature bindings from the provided [DiTree]
 * and keeps the lower UI and domain layers free from direct DI access.
 */
@Composable
fun HistoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = BegoTheme.sizes.screenHorizontalPadding,
                vertical = BegoTheme.sizes.screenVerticalPadding,
            ),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(BegoTheme.sizes.sectionSpacing),
        ) {
            BasicText(
                text = stringResource(Res.string.history_screen_label),
                style = BegoTheme.typography.bodyL.copy(
                    color = BegoTheme.palette.secondary,
                    fontWeight = FontWeight.Medium,
                ),
            )
            BegoHeaderText(text = stringResource(Res.string.history_placeholder_title))
            BegoBodyLargeText(text = stringResource(Res.string.history_placeholder_body))
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(BegoTheme.sizes.cardSpacing),
        ) {
            HistoryStatCard(
                eyebrow = stringResource(Res.string.history_placeholder_eyebrow),
                title = stringResource(Res.string.history_placeholder_next_title),
                body = stringResource(Res.string.history_placeholder_next_body),
            )
            HistoryStatCard(
                eyebrow = stringResource(Res.string.history_placeholder_eyebrow),
                title = stringResource(Res.string.history_placeholder_replay_title),
                body = stringResource(Res.string.history_placeholder_replay_body),
            )
        }
        Spacer(modifier = Modifier.height(BegoTheme.sizes.outlineWidth))
    }
}

@Composable
private fun HistoryStatCard(
    eyebrow: String,
    title: String,
    body: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(BegoTheme.shapes.panel)
            .background(BegoTheme.palette.panelSurface)
            .border(
                width = BegoTheme.sizes.outlineWidth,
                color = BegoTheme.palette.panelOutline,
                shape = BegoTheme.shapes.panel,
            )
            .padding(
                horizontal = BegoTheme.sizes.panelPaddingHorizontal,
                vertical = BegoTheme.sizes.panelPaddingVertical,
            ),
        verticalArrangement = Arrangement.spacedBy(BegoTheme.sizes.paddingVertical),
    ) {
        BasicText(
            text = eyebrow,
            style = BegoTheme.typography.bodyL.copy(
                color = BegoTheme.palette.accent,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        BegoHeaderText(text = title)
        BegoBodyLargeText(text = body)
    }
}
