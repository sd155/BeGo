package io.github.sd155.bego

import android.app.Application
import android.content.pm.ApplicationInfo
import io.github.sd155.bego.di.DiTree
import io.github.sd155.bego.di.DiTreeHolder
import io.github.sd155.bego.di.diTree
import io.github.sd155.bego.tracker.app.trackerModule
import io.github.sd155.bego.tracker.platform.api.TrackerAndroidComponentsBuilder
import io.github.sd155.logs.AndroidLoggerConfigurator
import io.github.sd155.logs.createAndroidLogger

internal class BegoApplication : Application(), DiTreeHolder {

    private lateinit var dependencies: DiTree
    override val diTree: DiTree by lazy { dependencies }

    override fun onCreate() {
        super.onCreate()
        dependencies = diTree {
            val trackerComponentsBuilder = TrackerAndroidComponentsBuilder()
            importAll(
                applicationModule(
                    appName = AppName(
                        name = applicationContext.resources.getString(R.string.app_name),
                        version = applicationContext.resources.getString(R.string.app_version),
                    ),
                    loggerBuilder = ::createAndroidLogger,
                ),
                trackerModule(
                    loggerBuilder = ::createAndroidLogger,
                    locationProviderBuilder = { logger ->
                        trackerComponentsBuilder.createLocationProvider(
                            applicationContext = applicationContext,
                            logger = logger,
                        )
                    },
                    platformHooksBuilder = { logger ->
                        trackerComponentsBuilder.createHooks(logger = logger)
                    },
                    platformBinding = { commonComponents ->
                        trackerComponentsBuilder.bind(
                            builder = this,
                            context = applicationContext,
                            components = commonComponents,
                        )
                    }
                ),
            )
        }
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
