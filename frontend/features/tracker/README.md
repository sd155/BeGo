# Tracker Module

Accurate running session tracking with smoothed GPS, speed, and pace display.  
Back to [BeGo frontend application](../../README.md)

## Features

- **Precise Timing**: Accurate time tracking with centisecond precision
- **Accurate Distance Tracking**: Uses a Kalman filter with velocity and bearing for robust, low-noise path smoothing.
- **Noise Rejection**: Ignores GPS noise and prevents distance drift when stationary.
- **Distance templates**: Supports most used race distances (1km, 5km, 10km, 21km, 42km).
- **Automatic Session Finish**: Stops tracking automatically when a target distance (default: 1000m) is reached.
- **Speed and Pace**: UI shows running session speed (km/h) and pace (min/km).
- **Multi-language Support**: Built-in support for English and Russian languages.

## API

- Common  
  - LocationProvider  
  - TrackerScreenRoute
  - composable TrackerScreen(): Unit
  - trackerModule((source: String) -> Logger, LocationProvider): DI.Module

- Android
  - GmsLocationProvider
  - AndroidTrackerModuleDi.onCreateActivity(ComponentActivity): Unit
  - AndroidTrackerModuleDi.onResumeActivity(ComponentActivity): Unit
  - AndroidTrackerModuleDi.onPauseActivity(ComponentActivity): Unit
  - AndroidTrackerModuleDi.onDestroyActivity(): Unit
  - AndroidTrackerModuleDi.onActivityResult(Int, Int): Unit
  - AndroidPermissionValidator(Logger?)
  - AndroidPermissionValidator.setup(ComponentActivity): Unit
  - AndroidPermissionValidator.check(Context, Array<String>): Boolean
  - suspend AndroidPermissionValidator.checkAndRequest(Context, Array<String>): Boolean

## Architecture

### Common code

#### UI Layer
- `TrackerScreen`: Main entry point composable for the tracker feature.
- `TrackerViewModel`: ViewModel handling tracker state and user intents.
- `TrackerViewState`: Immutable state representing the complete UI state.
- `TrackerView` : View rendering UI state.
- `TrackerViewIntent`: Sealed class defining all possible user actions.

#### Domain Layer
- `Tracker`: Main class for managing tracking sessions.
- `TrackerState`: Immutable state representing the current tracker state.
- `TrackPoint`: Data class representing a single location sample, including position, speed, bearing, and their accuracies.
- `KalmanLocationFilter`: Kalman filter for smoothing GPS data using position, speed, and bearing.

### Android platform
- `GmsLocationProvider` : Implementation of **LocationProvider** for Android platform backed up GMS location services.
- `TrackerForegroundService` : Implementation of Android foreground service to track session while in background.
- `AndroidPermissionValidator` : Helper for checking and requesting Android runtime location permissions.
- `AndroidTrackerModuleDi` : Android DI helper for wiring up the tracker module with the current activity.
