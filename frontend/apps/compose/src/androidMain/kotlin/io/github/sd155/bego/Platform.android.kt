package io.github.sd155.bego

import android.content.Context

internal class AndroidPlatformConfiguration(
    private val context: Context,
    override val appName: String,
    override val appVersion: String,
) : PlatformConfiguration
