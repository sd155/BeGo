# Dependency Injection Module

Thin dependency injection (DI) wrapper, based on [Kodein DI](https://kodein.org/di/). It hides the underlying DI engine from feature modules and exposes only the minimal BeGo DI API.  
Back to the [BeGo frontend application](../../README.md)

## Features
- **Thin wrapper**: Feature modules depend on `DiModule` and `DiTree`, not on Kodein types
- **Root composition**: The app composition root assembles modules into a single DI tree
- **Android bridge**: Android framework entry points can access the shared root through `DiTreeHolder`

## Target model
- `di-kodein` is infrastructure only. It owns the DI engine integration and exposes the BeGo DI API.
- Feature modules define registrations through `DiModule`, but do not import `org.kodein.*`.
- The app composition root assembles modules into a shared DI tree during startup.
- UI and domain code should use explicit constructor/factory dependencies where practical, while feature app-layers may resolve from `DiTree`.
- Android framework-created components use `DiTreeHolder` to access the shared root tree when constructor injection is unavailable.

## API
- Common
  - `DiTreeHolder`
  - `DiModule`
  - `DiTree`
  - `diModule(String, DiModuleBuilder.() -> Unit): DiModule`
  - `diTree(DiTreeBuilder.() -> Unit): DiTree`

## Dependencies
- [Kodein DI](https://kodein.org/di/) (see build.gradle.kts)
