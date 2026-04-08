package io.github.sd155.bego.tracker.app

import io.github.sd155.bego.di.DiModule
import io.github.sd155.bego.di.diModule
import io.github.sd155.bego.tracker.domain.Tracker
import io.github.sd155.logs.api.Logger

/**
 * Creates a dependency injection module for the tracker feature.
 * This module provides the necessary dependencies for the tracker functionality.
 *
 * @param loggerProvider A function that creates a logger instance for the tracker feature
 * @param locationProvider The platform-specific location provider to use
 * @param platformHooks The platform-specific tracker screen integration hooks
 * @return A DI module containing the tracker feature dependencies
 */
fun trackerModule(
    loggerProvider: (source: String) -> Logger,
    locationProvider: LocationProvider,
    platformHooks: TrackerPlatformHooks,
): DiModule = diModule(name = trackerModuleName) {
    bindSingleton<Logger>(tag = trackerModuleName) { loggerProvider("Tracker") }
    bindSingleton<LocationProvider> { locationProvider }
    bindSingleton<TrackerPlatformHooks> { platformHooks }
    bindSingleton<Tracker> {
        Tracker(
            logger = instance(tag = trackerModuleName),
            locationProvider = instance(),
        )
    }
}

internal const val trackerModuleName = "tracker"
