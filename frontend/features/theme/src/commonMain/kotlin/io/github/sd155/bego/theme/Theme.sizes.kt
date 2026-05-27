package io.github.sd155.bego.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Size definitions for the Bego application.
 * Defines the dimensions used for UI elements.
 *
 * @property screen The device screen size this set of sizes is designed for.
 * @property icon The standard size for icons.
 * @property paddingVertical The standard vertical padding.
 * @property paddingHorizontal The standard horizontal padding.
 */
class BegoSizes(
    val screen: DeviceScreen,
    val icon: Dp,
    val buttonWidth: Dp,
    val paddingVertical: Dp,
    val paddingHorizontal: Dp,
    val contentVerticalPadding: Dp,
    val dockReservedHeight: Dp,
    val dockBottomPadding: Dp,
    val dockPaddingHorizontal: Dp,
    val dockPaddingVertical: Dp,
    val dockItemPaddingHorizontal: Dp,
    val dockItemPaddingVertical: Dp,
    val dockSpacing: Dp,
    val dockIndicator: Dp,
    val screenHorizontalPadding: Dp,
    val screenVerticalPadding: Dp,
    val sectionSpacing: Dp,
    val cardSpacing: Dp,
    val panelPaddingHorizontal: Dp,
    val panelPaddingVertical: Dp,
    val outlineWidth: Dp,
)

internal fun begoSizes(screen: DeviceScreen) = when (screen) {
    DeviceScreen.Compact ->
        BegoSizes(
            screen = screen,
            icon = 32.dp,
            buttonWidth = 160.dp,
            paddingVertical = 8.dp,
            paddingHorizontal = 16.dp,
            contentVerticalPadding = 64.dp,
            dockReservedHeight = 116.dp,
            dockBottomPadding = 24.dp,
            dockPaddingHorizontal = 10.dp,
            dockPaddingVertical = 10.dp,
            dockItemPaddingHorizontal = 18.dp,
            dockItemPaddingVertical = 14.dp,
            dockSpacing = 10.dp,
            dockIndicator = 10.dp,
            screenHorizontalPadding = 24.dp,
            screenVerticalPadding = 32.dp,
            sectionSpacing = 12.dp,
            cardSpacing = 16.dp,
            panelPaddingHorizontal = 20.dp,
            panelPaddingVertical = 18.dp,
            outlineWidth = 1.dp,
        )
    DeviceScreen.Medium ->
        BegoSizes(
            screen = screen,
            icon = 48.dp,
            buttonWidth = 240.dp,
            paddingVertical = 12.dp,
            paddingHorizontal = 24.dp,
            contentVerticalPadding = 96.dp,
            dockReservedHeight = 148.dp,
            dockBottomPadding = 32.dp,
            dockPaddingHorizontal = 14.dp,
            dockPaddingVertical = 14.dp,
            dockItemPaddingHorizontal = 22.dp,
            dockItemPaddingVertical = 18.dp,
            dockSpacing = 12.dp,
            dockIndicator = 12.dp,
            screenHorizontalPadding = 36.dp,
            screenVerticalPadding = 40.dp,
            sectionSpacing = 16.dp,
            cardSpacing = 20.dp,
            panelPaddingHorizontal = 24.dp,
            panelPaddingVertical = 22.dp,
            outlineWidth = 1.dp,
        )
}

internal val LocalBegoSizes = staticCompositionLocalOf<BegoSizes> {
    error("No sizes provided")
}
