package io.github.sd155.bego

import android.app.Application

internal class BegoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PlatformDi.init(
            configuration = AndroidPlatformConfiguration(
                context = applicationContext,
                appName = applicationContext.resources.getString(R.string.app_name),
                appVersion = applicationContext.resources.getString(R.string.app_version),
            ),
        )
    }
}