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
    val paddingVertical: Dp,
    val paddingHorizontal: Dp,
)

internal fun begoSizes(screen: DeviceScreen) = when (screen) {
    DeviceScreen.Small ->
        BegoSizes(
            screen = screen,
            icon = 32.dp,
            paddingVertical = 8.dp,
            paddingHorizontal = 16.dp,
        )
    DeviceScreen.Medium ->
        BegoSizes(
            screen = screen,
            icon = 48.dp,
            paddingVertical = 12.dp,
            paddingHorizontal = 24.dp,
        )
}

internal val LocalBegoSizes = staticCompositionLocalOf<BegoSizes> {
    error("No sizes provided")
}