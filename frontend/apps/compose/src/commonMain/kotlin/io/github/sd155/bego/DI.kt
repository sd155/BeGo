package io.github.sd155.bego

import io.github.sd155.bego.di.Inject
import io.github.sd155.logs.api.Logger
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.singleton

internal const val applicationModuleName: String = "application"

internal fun initApplicationDi(
    applicationModule: DI.Module,
    trackerModule: DI.Module,
) {
    Inject.createDependencies(
        DI {
            importAll(
                applicationModule,
                trackerModule
            )
        }.direct
    )
}

internal fun applicationModule(
    appName: AppName,
    loggerProvider: (source: String) -> Logger,
) =
    DI.Module(name = applicationModuleName) {
        bind<AppName>() with singleton { appName }
        bind<Logger>(applicationModuleName) with singleton { loggerProvider("APP") }
    }