# BeGo Frontend

[BeGo](../README.md) is a running training application designed to help users track and improve their running performance. The frontend is built using Kotlin Multiplatform (KMP) technology, allowing for a consistent user experience across multiple platforms.

## Purpose
The application helps runners:
- Track their running sessions with precise timing and distance
- View real-time distance speed and pace

## Functionality
- **Running Tracker**: Accurate running session tracking with smoothed GPS, speed, and pace display
- **Stopwatch**: Precise timing for running sessions

## Supported Platforms
- **Android**: Full native support with Material Design 3
- **Aurora**: In progress
- **iOS**: In progress

## Frontend scheme
```text
/
├── apps/
│   ├── compose/               # KMP app module (entry point for front end app)
│   │   ├── src/                     
│   │   │   ├── commonMain/    # Shared code across platforms
│   │   │   ├── androidMain/   # Android-specific code
│   │   │   ├── desktopMain/   # Desktop-specific code (JVM) (In progress)
│   │   │   ├── wasmJsMain/   # Web-specific code (WASM/JS) (In progress)
│   │   │   └── iosMain/       # iOS-specific code (In progress)
│   │   └── build.gradle.kts   # KMP app module build configuration
│   │
│   └── ios/                   # iOS app module (In progress)
│
├── features/                     # KMP project features
│   ├── di-kodein/                # Shared DI module (Kodein DI)
│   ├── theme/                    # App theme module
│   ├── tracker/                  # Running tracker feature module
│   └── utils/                    # Multiplatform utilities: typed result, SafeContinuation, etc.
│
├── gradle/                       # Gradle wrapper
│   └── wrapper/
│   │   └── gradle-wrapper.properties # Gradle wrapper properties
│   └── libs.versions.toml        # KMP project dependencies and properties
│
├── build.gradle.kts              # KMP project build configuration
├── gradle.properties             # KMP project gradle properties
├── gradlew                       # KMP project gradle wrapper script (for *nix)
├── gradlew.bat                   # KMP project gradle wrapper script (for Windows)
└── settings.gradle.kts           # KMP project settings
```

## Frontend Features

- App theme system, [more details](./features/theme/README.md)
- Running tracker with smoothed GPS, speed, and pace, [more details](./features/tracker/README.md)
- Dependency injection system (Kodein DI), [more details](./features/di-kodein/README.md)
- Multiplatform utilities (typed result, SafeContinuation, etc.), [more details](./features/utils/README.md)