package io.github.sd155.bego.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Main theme composable for the Bego application.
 *
 * Provides a consistent design system across the app including colors, typography, shapes, sizes, and platform icons.
 * Should wrap your app's content at the root level.
 *
 * @param screen The device screen size to adapt the theme for. Defaults to [DeviceScreen.Small].
 * @param isDarkTheme Whether to use dark theme. Defaults to system preference.
 * @param platformIcons The [PlatformIcons] implementation for the current platform.
 * @param content The content to be themed.
 */
@Composable
fun BegoTheme(
    screen: DeviceScreen = DeviceScreen.Small,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    platformIcons: PlatformIcons,
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
        LocalPlatformIcons provides platformIcons,
        content = content
    )
}

/**
 * Object providing access to the current theme values.
 *
 * Use this to access theme properties (colors, typography, shapes, sizes, icons) within composables.
 *
 * Example:
 * ```kotlin
 * val color = BegoTheme.palette.primary
 * val icon = BegoTheme.platformIcons.check()
 * ```
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

    /**
     * Gets the current platform icons provider.
     * @return The current [PlatformIcons] instance.
     */
    val platformIcons: PlatformIcons
        @Composable
        get() = LocalPlatformIcons.current
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

/**
 * Interface for providing platform-specific icons to the Bego theme system.
 * Implement this interface to supply icons for different platforms (Android, iOS, etc).
 */
interface PlatformIcons {
    /** Returns the checkmark icon. */
    fun check(): ImageVector
    /** Returns the dropdown arrow icon. */
    fun dropDown(): ImageVector
}

internal val LocalPlatformIcons = staticCompositionLocalOf<PlatformIcons> {
    error("No platform icons provided")
}
