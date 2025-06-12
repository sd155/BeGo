package io.github.sd155.bego

import androidx.compose.runtime.*
import io.github.sd155.bego.theme.BegoTheme
import io.github.sd155.bego.theme.DeviceScreen

@Composable
internal fun App(
    screen: DeviceScreen,
) {
    BegoTheme(
        screen = screen,
    ) {
        AppNavGraph()
    }
}
