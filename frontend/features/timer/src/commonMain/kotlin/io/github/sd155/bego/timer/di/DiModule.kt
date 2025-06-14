package io.github.sd155.bego.timer.di

import io.github.sd155.bego.timer.domain.RunningTimer
import io.github.sd155.logs.api.Logger
import org.kodein.di.bind
import org.kodein.di.DI
import org.kodein.di.singleton

/**
 * Creates a dependency injection module for the timer feature.
 * This module provides the necessary dependencies for the timer functionality.
 *
 * @param loggerProvider A function that creates a logger instance for the timer feature
 * @return A DI module containing the timer feature dependencies
 */
fun timerModule(
    loggerProvider: (source: String) -> Logger,
) = DI.Module(name = "timer") {
    bind<RunningTimer>() with singleton { RunningTimer() }
    bind<Logger>() with singleton { loggerProvider("Timer") }
}