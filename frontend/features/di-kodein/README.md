# Dependency Injection Module

Simple dependency injection (DI) system, based on [Kodein DI](https://kodein.org/di/). It enables easy and type-safe access to shared dependencies across the application.  
Back to the [BeGo frontend application](../../README.md)

## Features
- **DI managing**: Singleton for managing the DI container
- **Dependency retrieval**: Access to the DI tree and instance retrieval

## API
- Common
  - `Inject.di: DirectDI`
  - `Inject.createDependencies(DirectDI): Unit`
  - `Inject.instance(Any?): T`

#### Usage
```kotlin
// Initialize dependencies (typically at app startup)
Inject.createDependencies(diTree)

// Retrieve an instance of a dependency
val myService: MyService = Inject.instance()
```

## Dependencies
- [Kodein DI](https://kodein.org/di/) (see build.gradle.kts)
