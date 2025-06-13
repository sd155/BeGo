package io.github.sd155.bego.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Typography system for the Bego application.
 * Defines text styles used throughout the app.
 *
 * @property header Text style for headers and titles.
 * @property label Text style for labels and buttons.
 * @property bodyL Text style for large body text.
 * @property bodyM Text style for medium body text.
 */
class BegoTypography(
    val header: TextStyle,
    val label: TextStyle,
    val bodyL: TextStyle,
    val bodyM: TextStyle,
)

internal fun begoTypography(screen: DeviceScreen) = when (screen) {
    DeviceScreen.Small ->
        BegoTypography(
            header = TextStyle(
                fontWeight = FontWeight.W400,
                fontSize = 64.sp,
            ),
            label = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 20.sp,
            ),
            bodyL = TextStyle(
                fontWeight = FontWeight.W400,
                fontSize = 28.sp,
            ),
            bodyM = TextStyle(
                fontWeight = FontWeight.W400,
                fontSize = 13.sp,
            ),
        )
    DeviceScreen.Medium ->
        BegoTypography(
            header = TextStyle(
                fontWeight = FontWeight.W400,
                fontSize = 96.sp,
            ),
            label = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 30.sp,
            ),
            bodyL = TextStyle(
                fontWeight = FontWeight.W400,
                fontSize = 42.sp,
            ),
            bodyM = TextStyle(
                fontWeight = FontWeight.W400,
                fontSize = 20.sp,
            ),
        )
}

internal val LocalBegoTypography = staticCompositionLocalOf<BegoTypography> {
    error("No typography provided")
}