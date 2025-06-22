# Tracker Module

The tracker module provides precise running session tracking for the [BeGo frontend application](../../README.md). It implements a clean architecture pattern with clear separation of concerns between UI and domain layers.

## Features

- **Precise Timing**: Accurate time tracking with centisecond precision
- **Accurate Distance Tracking**: Uses a Kalman filter with velocity and bearing for robust, low-noise path smoothing.
- **Noise Rejection**: Ignores GPS noise and prevents distance drift when stationary.
- **Automatic Session Finish**: Stops tracking automatically when a target distance (default: 1000m) is reached.
- **Speed and Pace**: UI shows running session speed (km/h) and pace (min/km).
- **Multi-language Support**: Built-in support for English and Russian languages.

## Architecture

### UI Layer
- `TrackerScreen`: Main entry point composable for the tracker feature.
- `TrackerViewModel`: ViewModel handling tracker state and user intents.
- `TrackerViewState`: Immutable state representing the complete UI state.
- `TrackerViewIntent`: Sealed class defining all possible user actions.

### Domain Layer
- `Tracker`: Main class for managing tracking sessions.
- `TrackerState`: Immutable state representing the current tracker state.
- `TrackPoint`: Data class representing a single location sample, including position, speed, bearing, and their accuracies.
- `KalmanLocationFilter`: Kalman filter for smoothing GPS data using position, speed, and bearing.
