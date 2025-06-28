package io.github.sd155.bego

import androidx.compose.runtime.*
import io.github.sd155.bego.theme.BegoTheme
import io.github.sd155.bego.theme.DeviceScreen
import io.github.sd155.bego.theme.PlatformIcons

@Composable
internal fun App(
    screen: DeviceScreen,
    platformIcons: PlatformIcons,
) {
    BegoTheme(
        screen = screen,
        platformIcons = platformIcons,
    ) {
        AppNavGraph()
    }
}
