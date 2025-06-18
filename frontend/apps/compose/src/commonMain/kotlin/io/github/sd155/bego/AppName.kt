package io.github.sd155.bego

import androidx.compose.runtime.staticCompositionLocalOf

internal data class AppName(
    val name: String,
    val version: String,
)

internal val LocalAppName = staticCompositionLocalOf<AppName> { error("no default platform") }
