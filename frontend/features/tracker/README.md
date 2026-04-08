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
  - `TrackerPlatformHooks`
  - `TrackerScreenRoute`
  - `TrackerScreenBindings`
  - `trackerScreenBindings(DiTree): TrackerScreenBindings`
  - composable `TrackerScreen(TrackerScreenBindings)`
  - `trackerModule((String) -> Logger, LocationProvider, TrackerPlatformHooks): DiModule`
- Android
  - `GmsLocationProvider`
  - `AndroidTrackerPlatformHooks`
  - `initializeAndroidTrackerRuntime(Context): Unit`
  - `AndroidPermissionValidator(ActivityResultLauncher<Array<String>>, Logger)`
  - `AndroidPermissionValidator.check(Context, Array<String>): Boolean`

## DI wiring contract
- The tracker module exports `trackerModule(...)` so the app composition root can register tracker dependencies in the shared DI tree.
- The tracker module exports `trackerScreenBindings(DiTree)` so app-level navigation can pass prebuilt screen dependencies to `TrackerScreen` without exposing internal UI or domain types.
- `TrackerScreenBindings` is public only for app-to-feature wiring. Its contents stay module-internal.
- `TrackerPlatformHooks` is public only for platform-specific construction and DI wiring. Its rendering and prerequisite methods are module-internal.
- Domain and UI implementation types such as `Tracker`, `TrackerViewModel`, `TrackerView`, `TrackerState`, and `TrackPoint` are internal.

## Structure

The codebase is primarily structured by platform or purpose (e.g., debug, test).  
Secondly, the codebase is structured by layers: application, domain, UI.  
Access modifiers are part of the architecture, not a style preference (see [project specification](../../../PROJECT.md)).
Types exposed only for platform wiring or DI composition are documented in the DI wiring contract above.

### Common code

#### UI Layer
- `TrackerScreen`: Main entry point composable for the tracker feature. Receives prebuilt screen bindings from the outer binding layer.
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
