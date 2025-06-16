# Tracker Module

The tracker module provides a precise stopwatch functionality with lap recording capabilities for the [BeGo frontend application](../../README.md). It implements a clean architecture pattern with clear separation of concerns between UI and domain layers.

## Features

- **Precise Timing**: Accurate time tracking with centisecond precision
- **Lap Recording**: Track individual lap times during a running session
- **Multi-language Support**: Built-in support for English and Russian languages

## Public API

The module exposes two public interfaces:
- `StopwatchScreen`: Main entry point that provides the complete stopwatch functionality
- `trackerModule`: Dependency injection module that provides tracker-related dependencies

## Components

### UI Layer
The UI layer follows the MVI (Model-View-Intent) pattern with unidirectional data flow:
- `StopwatchScreen`: Public API that encapsulates the stopwatch implementation
- `StopwatchView`: Core UI component that renders the stopwatch interface
- `StopwatchViewModel`: Processes user intents and manages UI state
- `StopwatchViewState`: Immutable state representing the complete UI state
- `StopwatchViewIntent`: Sealed class defining all possible user actions

### Domain Layer
The domain layer implements a state machine pattern:
- `Stopwatch`: State machine that manages stopwatch state transitions
- `StopwatchState`: Immutable state representing the current stopwatch state
- `Timer`: Internal class handling precise time tracking
