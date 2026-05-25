package io.github.sd155.bego.tracker.platform.app

import android.content.Context
import io.github.sd155.bego.di.DiModuleBuilder
import io.github.sd155.bego.tracker.app.LocationProvider
import io.github.sd155.bego.tracker.app.PlatformHooks
import io.github.sd155.bego.tracker.app.TrackerCommonComponents
import io.github.sd155.bego.tracker.platform.internal.AndroidGmsLocationProvider
import io.github.sd155.bego.tracker.platform.internal.AndroidHooks
import io.github.sd155.bego.tracker.platform.internal.AndroidRuntime
import io.github.sd155.logs.api.Logger

/**
 * Android-specific wiring helper for the tracker feature.
 */
class TrackerAndroidComponentsBuilder {

    /**
     * Creates the Android implementation of [LocationProvider].
     */
    fun createLocationProvider(
        applicationContext: Context,
        logger: Logger,
    ): LocationProvider =
        AndroidGmsLocationProvider(
            applicationContext = applicationContext,
            logger = logger,
        )

    /**
     * Creates Android UI hooks required by the tracker screen.
     */
    fun createHooks(logger: Logger): PlatformHooks =
        AndroidHooks(logger)

    /**
     * Registers Android runtime components that depend on common tracker objects.
     */
    fun bind(
        builder: DiModuleBuilder,
        context: Context,
        components: TrackerCommonComponents,
    ) {
        val runtime = AndroidRuntime(context, components.logger, components.tracker)
        builder.bindSingleton<AndroidRuntime> { runtime }
    }
}
