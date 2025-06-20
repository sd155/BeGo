package io.github.sd155.bego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.theme.screenSize
import io.github.sd155.bego.tracker.AndroidTrackerModuleDi

internal class BegoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidTrackerModuleDi().onCreateActivity(activity = this)
        setContent {
            CompositionLocalProvider(
                LocalAppName provides Inject.instance<AppName>(),
            ) {
                App(
                    screen = screenSize(),
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        AndroidTrackerModuleDi().onResumeActivity(activity = this)
    }

    override fun onPause() {
        super.onPause()
        AndroidTrackerModuleDi().onPauseActivity(activity = this)
    }
}
