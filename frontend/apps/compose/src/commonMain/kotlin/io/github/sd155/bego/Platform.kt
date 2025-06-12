package io.github.sd155.bego

import androidx.compose.runtime.staticCompositionLocalOf

internal interface PlatformConfiguration {
    val appName: String
    val appVersion: String
}

internal val LocalPlatform = staticCompositionLocalOf<PlatformConfiguration> { error("no default platform") }
