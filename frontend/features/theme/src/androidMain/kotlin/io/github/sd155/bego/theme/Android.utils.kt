package io.github.sd155.bego.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun screenSize(): DeviceScreen =
    if (LocalConfiguration.current.smallestScreenWidthDp < 600)
        DeviceScreen.Small
    else
        DeviceScreen.Medium