package io.github.sd155.bego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.theme.AndroidPlatformIcons
import io.github.sd155.bego.theme.screenSize

internal class BegoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalAppName provides Inject.instance<AppName>(),
            ) {
                App(
                    screen = screenSize(),
                    platformIcons = AndroidPlatformIcons()
                )
            }
        }
    }
}
