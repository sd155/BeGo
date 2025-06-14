# Dependency Injection Module (di-kodein)

This module provides a simple dependency injection (DI) system for the [BeGo frontend application](../../README.md), based on [Kodein DI](https://kodein.org/di/). It enables easy and type-safe access to shared dependencies across the application.

## Key Components

### Inject object
- Singleton for managing the DI container
- Provides access to the DI tree and instance retrieval

#### Usage
```kotlin
// Initialize dependencies (typically at app startup)
Inject.createDependencies(diTree)

// Retrieve an instance of a dependency
val myService: MyService = Inject.instance()
```

### API
- `Inject.di`: The current Kodein DI container (DirectDI)
- `Inject.createDependencies(tree: DirectDI)`: Initializes the DI container
- `Inject.instance<T>()`: Retrieves an instance of type T from the DI container

## Dependencies
- [Kodein DI](https://kodein.org/di/) (see build.gradle.kts)

## Notes
- This module is multiplatform and can be used in any KMP target.
- The DI container must be initialized before use, or an exception will be thrown. 