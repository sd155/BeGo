package io.github.sd155.bego

import io.github.sd155.bego.di.DiModule
import io.github.sd155.bego.di.DiTree
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.di.diModule
import io.github.sd155.bego.di.diTree
import io.github.sd155.bego.tracker.app.TrackerScreenBindings
import io.github.sd155.bego.tracker.app.trackerScreenBindings
import io.github.sd155.logs.api.Logger

internal const val applicationModuleName: String = "application"

internal data class AppDependencies(
    val appName: AppName,
    val trackerScreenBindings: TrackerScreenBindings,
)

internal fun initApplicationDi(
    applicationModule: DiModule,
    trackerModule: DiModule,
): AppDependencies {
    val tree = diTree {
        importAll(
            applicationModule,
            trackerModule
        )
    }
    Inject.createDependencies(tree)
    return appDependencies(tree)
}

private fun appDependencies(tree: DiTree): AppDependencies =
    AppDependencies(
        appName = tree.instance(),
        trackerScreenBindings = trackerScreenBindings(tree),
    )

internal fun applicationModule(
    appName: AppName,
    loggerProvider: (source: String) -> Logger,
) =
    diModule(name = applicationModuleName) {
        bindSingleton<AppName> { appName }
        bindSingleton<Logger>(tag = applicationModuleName) { loggerProvider("APP") }
    }
