package io.github.sd155.bego.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

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

object BegoTheme {
    val palette: BegoPalette
        @Composable
        get() = LocalBegoPalette.current

    val typography: BegoTypography
        @Composable
        get() = LocalBegoTypography.current

    val shapes: BegoShapes
        @Composable
        get() = LocalBegoShapes.current

    val sizes: BegoSizes
        @Composable
        get() = LocalBegoSizes.current
}

enum class DeviceScreen {
    Small, Medium
}