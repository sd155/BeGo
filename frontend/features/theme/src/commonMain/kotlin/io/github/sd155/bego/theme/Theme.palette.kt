package io.github.sd155.bego.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Color palette for the Bego application.
 * Defines the main colors used throughout the app.
 *
 * @property primary The primary color used for main UI elements.
 * @property secondary The secondary color used for supporting UI elements.
 * @property background The background color for screens and surfaces.
 * @property accent The accent color used for highlights and CTAs.
 * @property warning The warning color used for error states and alerts.
 * @property onAccent The color to use on top of accent color for contrast.
 */
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
            secondary = Colors.colorNeutral400Light,
            background = Colors.colorNeutral700Dark,
            accent = Colors.colorBlue700,
            warning = Colors.colorRed700,
            onAccent = Colors.colorNeutral700Light,
        )
    false ->
        BegoPalette(
            primary = Colors.colorNeutral700Dark,
            secondary = Colors.colorNeutral400Dark,
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
    val colorNeutral400Light = Color(0x66FFFFFF)
    val colorNeutral700Dark = Color(0xFF1C1E28)
    val colorNeutral400Dark = Color(0x661C1E28)
    val colorRed700 = Color(0xFFED4747)
}