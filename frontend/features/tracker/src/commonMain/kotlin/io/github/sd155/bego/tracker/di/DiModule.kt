package io.github.sd155.bego.tracker.di

import io.github.sd155.bego.tracker.domain.Stopwatch
import io.github.sd155.logs.api.Logger
import org.kodein.di.bind
import org.kodein.di.DI
import org.kodein.di.singleton

/**
 * Creates a dependency injection module for the tracker feature.
 * This module provides the necessary dependencies for the tracker functionality.
 *
 * @param loggerProvider A function that creates a logger instance for the tracker feature
 * @return A DI module containing the tracker feature dependencies
 */
fun trackerModule(
    loggerProvider: (source: String) -> Logger,
) = DI.Module(name = moduleName) {
    bind<Stopwatch>() with singleton { Stopwatch() }
    bind<Logger>(moduleName) with singleton { loggerProvider("Timer") }
}

internal val moduleName = "tracker"