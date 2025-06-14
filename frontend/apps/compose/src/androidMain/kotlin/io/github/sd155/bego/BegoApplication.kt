package io.github.sd155.bego

import android.app.Application
import android.content.pm.ApplicationInfo
import io.github.sd155.logs.AndroidLoggerConfigurator
import io.github.sd155.logs.createAndroidLogger

internal class BegoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PlatformDi.init(
            configuration = AndroidPlatformConfiguration(
                context = applicationContext,
                appName = applicationContext.resources.getString(R.string.app_name),
                appVersion = applicationContext.resources.getString(R.string.app_version),
            ),
            loggerProvider = ::createAndroidLogger
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