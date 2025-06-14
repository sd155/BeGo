package io.github.sd155.bego

import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.timer.di.timerModule
import io.github.sd155.logs.api.Logger
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.singleton

internal object PlatformDi {

    fun init(
        configuration: PlatformConfiguration,
        loggerProvider: (source: String) -> Logger,
    ) {
        val coreModule = DI.Module("core") {
            bind<PlatformConfiguration>() with singleton { configuration }
        }

        Inject.createDependencies(
            DI {
                importAll(
                    coreModule,
                    timerModule(loggerProvider)
                )
            }.direct
        )
    }
}