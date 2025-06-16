package io.github.sd155.bego

import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.di.trackerModule
import io.github.sd155.logs.api.Logger
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.singleton

internal object PlatformDi {
    const val APPLICATION_MODULE_NAME: String = "application"

    fun init(
        configuration: PlatformConfiguration,
        loggerProvider: (source: String) -> Logger,
    ) {
        val applicationModule = DI.Module(APPLICATION_MODULE_NAME) {
            bind<PlatformConfiguration>() with singleton { configuration }
            bind<Logger>(APPLICATION_MODULE_NAME) with singleton { loggerProvider("APP") }
        }

        Inject.createDependencies(
            DI {
                importAll(
                    applicationModule,
                    trackerModule(loggerProvider)
                )
            }.direct
        )
    }
}