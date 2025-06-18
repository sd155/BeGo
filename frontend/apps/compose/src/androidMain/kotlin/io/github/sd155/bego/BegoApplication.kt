package io.github.sd155.bego

import android.app.Application
import android.content.pm.ApplicationInfo
import io.github.sd155.bego.tracker.GmsLocationProvider
import io.github.sd155.bego.tracker.di.trackerModule
import io.github.sd155.logs.AndroidLoggerConfigurator
import io.github.sd155.logs.createAndroidLogger

internal class BegoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initApplicationDi(
            applicationModule = applicationModule(
                appName = AppName(
                    name = applicationContext.resources.getString(R.string.app_name),
                    version = applicationContext.resources.getString(R.string.app_version),
                ),
                loggerProvider = ::createAndroidLogger,
            ),
            trackerModule = trackerModule(
                loggerProvider = ::createAndroidLogger,
                locationProvider = GmsLocationProvider(),
            )
        )
        AndroidLoggerConfigurator().apply {
            if (isDebuggable()) {
                enableLogcatLogging()
                enableTraceLogging()
                enableDebugLogging()
            }
        }
    }

    private fun isDebuggable(): Boolean =
        applicationContext.applicationInfo
            .flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
}