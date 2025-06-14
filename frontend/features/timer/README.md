# Timer Module

The timer module provides a precise stopwatch functionality with lap recording capabilities for the [BeGo frontend application](../../README.md). It implements a clean architecture pattern with clear separation of concerns between UI and domain layers.

## Features

- **Precise Timing**: Accurate time tracking with centisecond precision
- **Lap Recording**: Track individual lap times during a running session
- **Multi-language Support**: Built-in support for English and Russian languages

## Public API

The module exposes two public interfaces:
- `TimerScreen`: Main entry point that provides the complete timer functionality
- `timerModule`: Dependency injection module that provides timer-related dependencies

## Components

### UI Layer
The UI layer follows the MVI (Model-View-Intent) pattern with unidirectional data flow:
- `TimerScreen`: Public API that encapsulates the timer implementation
- `TimerView`: Core UI component that renders the timer interface
- `TimerViewModel`: Processes user intents and manages UI state
- `TimerViewState`: Immutable state representing the complete UI state
- `TimerViewIntent`: Sealed class defining all possible user actions

### Domain Layer
The domain layer implements a state machine pattern:
- `RunningTimer`: State machine that manages timer state transitions
- `RunningState`: Immutable state representing the current timer state
- `Timer`: Internal class handling precise time tracking
