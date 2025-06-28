package io.github.sd155.bego.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Determines the appropriate device screen size based on the current Android configuration.
 * Uses the smallest screen width to determine if the device is a phone (Small) or tablet (Medium).
 * 
 * @return [DeviceScreen.Small] for phones (width < 600dp) or [DeviceScreen.Medium] for tablets (width >= 600dp)
 */
@Composable
fun screenSize(): DeviceScreen =
    if (LocalConfiguration.current.smallestScreenWidthDp < 600)
        DeviceScreen.Small
    else
        DeviceScreen.Medium

class AndroidPlatformIcons : PlatformIcons {

    override fun check(): ImageVector = Icons.Default.Check
    override fun dropDown(): ImageVector = Icons.Default.ArrowDropDown
}