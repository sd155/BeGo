package io.github.sd155.bego.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class BegoPalette(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val accent: Color,
    val warning: Color,
    val onAccent: Color,
)

internal fun begoPalette(isDarkTheme: Boolean) = when (isDarkTheme) {
    true ->
        BegoPalette(
            primary = Colors.colorNeutral700Light,
            secondary = Colors.colorNeutral300Light,
            background = Colors.colorNeutral700Dark,
            accent = Colors.colorBlue700,
            warning = Colors.colorRed700,
            onAccent = Colors.colorNeutral700Light,
        )
    false ->
        BegoPalette(
            primary = Colors.colorNeutral700Dark,
            secondary = Colors.colorNeutral300Dark,
            background = Colors.colorNeutral700Light,
            accent = Colors.colorBlue700,
            warning = Colors.colorRed700,
            onAccent = Colors.colorNeutral700Light,
        )
}

internal val LocalBegoPalette = staticCompositionLocalOf<BegoPalette> {
    error("No colors provided")
}

private object Colors {
    val colorBlue700 = Color(0xFF4C6AFF)
    val colorNeutral700Light = Color(0xFFFFFFFF)
    val colorNeutral300Light = Color(0x29FFFFFF)
    val colorNeutral700Dark = Color(0xFF1C1E28)
    val colorNeutral300Dark = Color(0x291C1E28)
    val colorRed700 = Color(0xFFED4747)
}