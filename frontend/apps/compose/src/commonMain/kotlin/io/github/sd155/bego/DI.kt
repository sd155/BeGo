package io.github.sd155.bego

import io.github.sd155.bego.di.DiModule
import io.github.sd155.bego.di.diModule
import io.github.sd155.logs.api.Logger

internal const val applicationModuleName: String = "application"

internal fun applicationModule(
    appName: AppName,
    loggerBuilder: (source: String) -> Logger,
): DiModule =
    diModule(name = applicationModuleName) {
        bindSingleton<AppName> { appName }
        bindSingleton<Logger>(tag = applicationModuleName) { loggerBuilder("APP") }
    }
