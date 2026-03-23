# Tracker Module

Accurate running session tracking with smoothed GPS, speed, and pace display.  
Back to the [BeGo frontend application](../../README.md)

## Features
- **Precise Timing**: Accurate time tracking with centisecond precision
- **Accurate Distance Tracking**: Uses a Kalman filter with velocity and bearing for robust, low-noise path smoothing.
- **Noise Rejection**: Ignores GPS noise and prevents distance drift when stationary.
- **Distance presets**: Supports most popular race distances (1km, 5km, 10km, 21km, 42km).
- **Automatic Session Finish**: Stops tracking automatically when a target distance (default: 1000m) is reached.
- **Speed and Pace**: UI displays running session speed (km/h) and pace (min/km).
- **Multi-language Support**: Built-in support for English and Russian languages.

## API
- Common  
  - `LocationProvider`  
  - `LocationPrerequisites`
  - `PlatformTrackerRememberer`
  - `TrackerScreenRoute`
  - composable `TrackerScreen()`
  - `trackerModule((String) -> Logger, LocationProvider, PlatformTrackerRememberer): DI.Module`
- Android
  - `GmsLocationProvider`
  - `initializeAndroidTrackerRuntime(Context): Unit`
  - `AndroidLocationPrerequisites(ComponentActivity, Logger)`
  - `AndroidTrackerRememberer()`
  - `AndroidPermissionValidator(ComponentActivity, Logger?)`
  - `AndroidPermissionValidator.check(Context, Array<String>): Boolean`
  - suspend `AndroidPermissionValidator.checkAndRequest(Context, Array<String>): Boolean`

## Visibility Rules
- Access modifiers are architectural in this module.
- `public` is allowed only for the tracker module API and for platform-specific entry points that must be instantiated from another module, such as Android wiring classes passed into DI.
- `internal` is the default for runtime logic, UI state, domain services, and operational methods that must not leak through the module boundary.
- A `public` type may still have `internal` members when the type itself must be created from outside the module, but its behavior is intended to be consumed only inside the module.

## Structure

The codebase is primarily structured by platform or purpose (e.g., debug, test).  
Secondly, the codebase is structured by layers: application, domain, UI.

### Common code

#### UI Layer
- `TrackerScreen`: Main entry point composable for the tracker feature.
- `TrackerViewModel`: ViewModel handling tracker state and user intents.
- `TrackerViewState`: Immutable state representing the complete UI state.
- `TrackerView`: View rendering UI state.
- `TrackerViewIntent`: Sealed class defining all possible user actions.

#### Domain Layer
- `Tracker`: Main class for managing tracking sessions.
- `TrackerState`: Immutable state representing the current tracker state.
- `TrackPoint`: Data class representing a single location sample, including position, speed, bearing, and their accuracies.
- `KalmanLocationFilter`: Kalman filter for smoothing GPS data using position, speed, and bearing.

### Android platform
- `GmsLocationProvider`: Implementation of **LocationProvider** for Android platform backed by GMS location services.
- `AndroidTrackerPrerequisites`: Activity-scoped permissions and settings coordinator used before starting tracking.
- `AndroidTrackerRuntime`: Process lifecycle observer that manages background foreground-service state for active tracking sessions.
- `TrackerForegroundService`: Android foreground service used when tracking continues in background.
- `AndroidPermissionValidator`: Helper for checking and requesting Android runtime location permissions.
