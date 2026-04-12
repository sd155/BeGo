package io.github.sd155.bego.tracker.platform.api

import android.content.Context
import io.github.sd155.bego.di.DiModuleBuilder
import io.github.sd155.bego.tracker.app.LocationProvider
import io.github.sd155.bego.tracker.app.PlatformHooks
import io.github.sd155.bego.tracker.app.TrackerCommonComponents
import io.github.sd155.bego.tracker.platform.internal.AndroidGmsLocationProvider
import io.github.sd155.bego.tracker.platform.internal.AndroidHooks
import io.github.sd155.bego.tracker.platform.internal.AndroidRuntime
import io.github.sd155.logs.api.Logger

class TrackerAndroidComponentsBuilder {

    fun createLocationProvider(
        applicationContext: Context,
        logger: Logger,
    ): LocationProvider =
        AndroidGmsLocationProvider(
            applicationContext = applicationContext,
            logger = logger,
        )

    fun createHooks(logger: Logger): PlatformHooks =
        AndroidHooks(logger)

    fun bind(
        builder: DiModuleBuilder,
        context: Context,
        components: TrackerCommonComponents,
    ) {
        val runtime = AndroidRuntime(context, components.logger, components.tracker)
        builder.bindSingleton<AndroidRuntime> { runtime }
    }
}