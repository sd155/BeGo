# Result Module

This module provides a typed result abstraction for handling success and failure cases in a type-safe, multiplatform way. It is used throughout the [BeGo frontend application](../../README.md) for error handling and functional-style result chaining.

## Features
- Typed result type: `Result<F, S>` for error (`F`) or success (`S`)
- Extension functions for easy creation and chaining
- Multiplatform (Kotlin/JVM, Kotlin/Native, Kotlin/JS)
