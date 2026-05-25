package io.github.sd155.bego

import android.app.Application
import android.content.pm.ApplicationInfo
import io.github.sd155.bego.di.DiTree
import io.github.sd155.bego.di.DiTreeHolder
import io.github.sd155.bego.di.diTree
import io.github.sd155.bego.history.app.SessionPointConsumer
import io.github.sd155.bego.history.app.historyModule
import io.github.sd155.bego.history.platform.app.HistoryAndroidComponentsBuilder
import io.github.sd155.bego.tracker.app.trackerModule
import io.github.sd155.bego.tracker.platform.app.TrackerAndroidComponentsBuilder
import io.github.sd155.logs.AndroidLoggerConfigurator
import io.github.sd155.logs.createAndroidLogger

internal class BegoApplication : Application(), DiTreeHolder {

    private lateinit var dependencies: DiTree
    override val diTree: DiTree by lazy { dependencies }

    override fun onCreate() {
        super.onCreate()
        dependencies = diTree {
            val trackerComponentsBuilder = TrackerAndroidComponentsBuilder()
            val historyComponentsBuilder = HistoryAndroidComponentsBuilder()
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
                    sessionWriter = { point -> diTree.instance<SessionPointConsumer>().consume(point) },
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
                historyModule(
                    loggerBuilder = ::createAndroidLogger,
                    repositoryBuilder = { logger ->
                        historyComponentsBuilder.createRepository(
                            applicationContext = applicationContext,
                            logger = logger,
                        )
                    }
                )
            )
        }
        AndroidLoggerConfigurator().apply {
            if (isDebuggable()) {
                enableLogcatLogging()
                enableFileLogging(
                    appContext = applicationContext,
                    external = true,
                )
                enableTraceLogging()
                enableDebugLogging()
            }
        }
    }

    private fun isDebuggable(): Boolean =
        applicationContext.applicationInfo
            .flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
}
