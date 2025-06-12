package io.github.sd155.bego.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Main theme composable for the Bego application.
 * Provides a consistent design system across the app including colors, typography, shapes, and sizes.
 *
 * @param screen The device screen size to adapt the theme for. Defaults to [DeviceScreen.Small].
 * @param isDarkTheme Whether to use dark theme. Defaults to system preference.
 * @param content The content to be themed.
 */
@Composable
fun BegoTheme(
    screen: DeviceScreen = DeviceScreen.Small,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = begoPalette(isDarkTheme = isDarkTheme)
    val typography = begoTypography(screen)
    val shapes = BegoShapes()
    val sizes = begoSizes(screen)

    CompositionLocalProvider(
        LocalBegoPalette provides colors,
        LocalBegoTypography provides typography,
        LocalBegoShapes provides shapes,
        LocalBegoSizes provides sizes,
        content = content
    )
}

/**
 * Object providing access to the current theme values.
 * Use this to access theme properties within composables.
 */
object BegoTheme {
    /**
     * Gets the current color palette.
     * @return The current [BegoPalette] instance.
     */
    val palette: BegoPalette
        @Composable
        get() = LocalBegoPalette.current

    /**
     * Gets the current typography settings.
     * @return The current [BegoTypography] instance.
     */
    val typography: BegoTypography
        @Composable
        get() = LocalBegoTypography.current

    /**
     * Gets the current shape definitions.
     * @return The current [BegoShapes] instance.
     */
    val shapes: BegoShapes
        @Composable
        get() = LocalBegoShapes.current

    /**
     * Gets the current size definitions.
     * @return The current [BegoSizes] instance.
     */
    val sizes: BegoSizes
        @Composable
        get() = LocalBegoSizes.current
}

/**
 * Enum representing different device screen sizes.
 * Used to adapt the theme for different screen sizes.
 */
enum class DeviceScreen {
    /** Small screen size (e.g., phones) */
    Small,
    /** Medium screen size (e.g., tablets) */
    Medium
}