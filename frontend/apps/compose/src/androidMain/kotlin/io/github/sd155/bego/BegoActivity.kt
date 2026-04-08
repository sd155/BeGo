package io.github.sd155.bego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.sd155.bego.theme.AndroidPlatformIcons
import io.github.sd155.bego.theme.screenSize

internal class BegoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dependencies = (application as BegoApplication).dependencies
        setContent {
            App(
                screen = screenSize(),
                platformIcons = AndroidPlatformIcons(),
                dependencies = dependencies,
            )
        }
    }
}
