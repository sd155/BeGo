package io.github.sd155.bego.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.FloatingDock(
    content: @Composable (RowScope.() -> Unit),
) {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(bottom = BegoTheme.sizes.dockBottomPadding)
            .clip(shape = BegoTheme.shapes.dock)
            .background(BegoTheme.palette.dockSurface)
            .border(
                width = BegoTheme.sizes.outlineWidth,
                color = BegoTheme.palette.dockOutline,
                shape = BegoTheme.shapes.dock,
            )
            .padding(
                horizontal = BegoTheme.sizes.dockPaddingHorizontal,
                vertical = BegoTheme.sizes.dockPaddingVertical,
            ),
        horizontalArrangement = Arrangement.spacedBy(BegoTheme.sizes.dockSpacing),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
fun DockItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val containerColor =
        if (selected) BegoTheme.palette.accent
        else Color.Transparent
    val contentColor =
        if (selected) BegoTheme.palette.onAccent
        else BegoTheme.palette.primary
    val indicatorColor =
        if (selected) BegoTheme.palette.onAccent.copy(alpha = 0.95f)
        else BegoTheme.palette.dockIndicatorInactive

    Row(
        modifier = Modifier
            .clip(shape = BegoTheme.shapes.dockItem)
            .clickable(onClick = onClick)
            .background(containerColor)
            .padding(
                horizontal = BegoTheme.sizes.dockItemPaddingHorizontal,
                vertical = BegoTheme.sizes.dockItemPaddingVertical,
            ),
        horizontalArrangement = Arrangement.spacedBy(BegoTheme.sizes.dockSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(BegoTheme.sizes.dockIndicator)
                .clip(CircleShape)
                .background(indicatorColor),
        )
        BasicText(
            text = label,
            style = BegoTheme.typography.bodyL.copy(
                color = contentColor,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}
