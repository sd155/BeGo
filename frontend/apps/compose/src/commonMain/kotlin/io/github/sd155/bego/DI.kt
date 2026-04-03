package io.github.sd155.bego

import io.github.sd155.bego.di.DiModule
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.di.diModule
import io.github.sd155.bego.di.diTree
import io.github.sd155.logs.api.Logger

internal const val applicationModuleName: String = "application"

internal fun initApplicationDi(
    applicationModule: DiModule,
    trackerModule: DiModule,
) {
    Inject.createDependencies(
        diTree {
            importAll(
                applicationModule,
                trackerModule
            )
        }
    )
}

internal fun applicationModule(
    appName: AppName,
    loggerProvider: (source: String) -> Logger,
) =
    diModule(name = applicationModuleName) {
        bindSingleton<AppName> { appName }
        bindSingleton<Logger>(tag = applicationModuleName) { loggerProvider("APP") }
    }
