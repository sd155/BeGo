package io.github.sd155.bego.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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
                fontWeight = FontWeight.W600,
                fontSize = 32.sp,
            ),
            label = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
            ),
            bodyL = TextStyle(
                fontWeight = FontWeight.W400,
                fontSize = 24.sp,
            ),
            bodyM = TextStyle(
                fontWeight = FontWeight.W400,
                fontSize = 13.sp,
            ),
        )
    DeviceScreen.Medium ->
        BegoTypography(
            header = TextStyle(
                fontWeight = FontWeight.W600,
                fontSize = 48.sp,
            ),
            label = TextStyle(
                fontWeight = FontWeight.W500,
                fontSize = 24.sp,
            ),
            bodyL = TextStyle(
                fontWeight = FontWeight.W400,
                fontSize = 36.sp,
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