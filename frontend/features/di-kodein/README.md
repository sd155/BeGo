# Dependency Injection Module

Thin dependency injection (DI) wrapper, based on [Kodein DI](https://kodein.org/di/). It hides the underlying DI engine from feature modules and exposes only the minimal BeGo DI API.  
Back to the [BeGo frontend application](../../README.md)

## Features
- **Thin wrapper**: Feature modules depend on `DiModule` and `DiTree`, not on Kodein types
- **Root composition**: The application root assembles modules into a single DI tree
- **Transition support**: `Inject` remains available as a temporary global entry point during migration away from service locator usage

## Target model
- `di-kodein` is infrastructure only. It owns the DI engine integration and exposes the BeGo DI API.
- Feature modules define registrations through `DiModule`, but do not import `org.kodein.*`.
- The app composition root assembles modules and initializes the shared DI tree once during startup.
- UI and domain code should move toward explicit constructor/factory dependencies instead of resolving from `Inject`.

## API
- Common
  - `DiModule`
  - `DiTree`
  - `diModule(String, DiModuleBuilder.() -> Unit): DiModule`
  - `diTree(DiTreeBuilder.() -> Unit): DiTree`
  - `Inject.di: DiTree`
  - `Inject.createDependencies(DiTree): Unit`
  - `Inject.instance(Any?): T`

#### Usage
```kotlin
val appModule = diModule(name = "application") {
    bindSingleton<AppName> { appName }
}

val tree = diTree {
    importAll(appModule)
}

// Initialize dependencies once at app startup
Inject.createDependencies(tree)

// Retrieve an instance of a dependency
val myService: MyService = Inject.instance()
```

## Dependencies
- [Kodein DI](https://kodein.org/di/) (see build.gradle.kts)
