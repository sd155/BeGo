package io.github.sd155.bego.tracker.app

import io.github.sd155.bego.di.DiModule
import io.github.sd155.bego.di.DiModuleBuilder
import io.github.sd155.bego.di.diModule
import io.github.sd155.bego.tracker.domain.Tracker
import io.github.sd155.logs.api.Logger

/**
 * Creates a dependency injection module for the tracker feature.
 * This module provides the necessary dependencies for the tracker functionality.
 *
 * @param loggerBuilder A function that creates a logger instance for the tracker feature
 * @param locationProviderBuilder Creates the platform-specific location provider using the tracker logger
 * @param platformHooksBuilder Creates the platform-specific tracker screen hooks using the tracker logger
 * @param platformBinding Optional platform-specific binding step that can register additional dependencies
 * using already-created common tracker components
 * @return A DI module containing the tracker feature dependencies
 */
fun trackerModule(
    loggerBuilder: (source: String) -> Logger,
    locationProviderBuilder: (logger: Logger) -> LocationProvider,
    platformHooksBuilder: (logger: Logger) -> PlatformHooks,
    platformBinding: DiModuleBuilder.(components: TrackerCommonComponents) -> Unit = {},
): DiModule = diModule(name = trackerModuleName) {
    val logger = loggerBuilder("Tracker")
    val locationProvider = locationProviderBuilder(logger)
    val hooks = platformHooksBuilder(logger)
    val tracker = Tracker(
        logger = logger,
        locationProvider = locationProvider,
    )
    bindSingleton<Logger>(tag = trackerModuleName) { logger }
    bindSingleton<LocationProvider> { locationProvider }
    bindSingleton<PlatformHooks> { hooks }
    bindSingleton<Tracker> { tracker }
    platformBinding(this, TrackerCommonComponents(
        logger = logger,
        tracker = tracker,
    ))
}

/**
 * Common tracker components created before platform-specific bindings are applied.
 *
 * This type is public only to support platform binding extension from outside the module.
 */
class TrackerCommonComponents internal constructor(
    internal val logger: Logger,
    internal val tracker: Tracker,
)

internal const val trackerModuleName = "tracker"
