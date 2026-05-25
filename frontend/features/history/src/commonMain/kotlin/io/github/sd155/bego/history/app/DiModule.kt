package io.github.sd155.bego.history.app

import io.github.sd155.bego.di.DiModule
import io.github.sd155.bego.di.diModule
import io.github.sd155.logs.api.Logger

/**
 * Creates the DI module for history session-point persistence.
 */
fun historyModule(
    loggerBuilder: (source: String) -> Logger,
    repositoryBuilder: (logger: Logger) -> SessionRepository,
): DiModule = diModule(name = historyModuleName) {
    val logger = loggerBuilder("History")
    val repository = repositoryBuilder(logger)
    bindSingleton<Logger>(tag = historyModuleName) { logger }
    bindSingleton<SessionRepository> { repository }
    bindSingleton<SessionPointConsumer> { repository }
}

internal const val historyModuleName = "history"
