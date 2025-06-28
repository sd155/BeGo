package io.github.sd155.bego

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.theme.AndroidPlatformIcons
import io.github.sd155.bego.theme.screenSize
import io.github.sd155.bego.tracker.AndroidTrackerModuleDi

internal class BegoActivity : ComponentActivity() {
    private lateinit var trackerModuleDi: AndroidTrackerModuleDi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackerModuleDi = AndroidTrackerModuleDi()
        trackerModuleDi.onCreateActivity(activity = this)
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

    override fun onResume() {
        super.onResume()
        trackerModuleDi.onResumeActivity(activity = this)
    }

    override fun onPause() {
        super.onPause()
        trackerModuleDi.onPauseActivity(activity = this)
    }

    override fun onDestroy() {
        super.onDestroy()
        trackerModuleDi.onDestroyActivity()
    }

    // Deprecation is in place due to using `ResolvableApiException.startResolutionForResult(activity: Activity, requestCode: Int)` method,
    // which forces to manage request code manually, thus preventing use of ActivityResultContract.
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        trackerModuleDi.onActivityResult(requestCode, resultCode)
    }
}
