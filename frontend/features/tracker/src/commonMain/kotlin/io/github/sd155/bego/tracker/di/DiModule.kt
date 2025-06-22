package io.github.sd155.bego.tracker.di

import io.github.sd155.bego.tracker.domain.LocationProvider
import io.github.sd155.bego.tracker.domain.Tracker
import io.github.sd155.logs.api.Logger
import org.kodein.di.bind
import org.kodein.di.DI
import org.kodein.di.singleton

/**
 * Creates a dependency injection module for the tracker feature.
 * This module provides the necessary dependencies for the tracker functionality.
 *
 * @param loggerProvider A function that creates a logger instance for the tracker feature
 * @param locationProvider The platform-specific location provider to use
 * @return A DI module containing the tracker feature dependencies
 */
fun trackerModule(
    loggerProvider: (source: String) -> Logger,
    locationProvider: LocationProvider,
) = DI.Module(name = trackerModuleName) {
    bind<Logger>(trackerModuleName) with singleton { loggerProvider("Tracker") }
    bind<LocationProvider>() with singleton { locationProvider }
    bind<Tracker>() with singleton { Tracker() }
}

internal const val trackerModuleName = "tracker"