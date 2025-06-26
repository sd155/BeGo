# Utils Module

This module provides multiplatform utility abstractions for error handling and coroutine safety, used throughout the [BeGo frontend application](../../README.md).

## Features
- **Typed result type**: `Result<F, S>` for error (`F`) or success (`S`). Extension functions for easy creation and chaining.
- **SafeContinuation**: Utility for safe, atomic coroutine continuation management

---

## Typed Result (`Result<F, S>`)  
A sealed class for representing success or failure in a type-safe, functional style.

### Example
```kotlin
val result: Result<String, Int> = 42.asSuccess()
val error: Result<String, Int> = "Oops".asFailure()

val chained = result.next { value -> (value + 1).asSuccess() }
val folded = result.fold(
    onSuccess = { "Value: $it" },
    onFailure = { "Error: $it" }
)
```

---

## SafeContinuation<T>
A utility for safe, atomic management of coroutine continuations. Prevents leaks and double-resume bugs in async/callback code.

### Example
```kotlin
val safe = SafeContinuation<Result<String, Unit>>()
safe.store(continuation)
// ... later, from a callback:
safe.resume(Result.success(Unit))
// or, to clean up:
safe.close()
```
