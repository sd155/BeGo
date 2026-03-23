# BeGo Frontend

[BeGo](../README.md) is a running training application designed to help users track and improve their running performance. The frontend is built using Kotlin Multiplatform (KMP) technology, allowing for a consistent user experience across multiple platforms.

## Supported Platforms
- **Android**: Full native support with Material Design 3
- **Aurora**: In progress
- **iOS**: In progress

## Functionality
- **Running Tracker**: Accurate running session tracking with smoothed GPS, speed, and pace display
- **Stopwatch**: Precise timing for running sessions

## Frontend scheme
```text
/
├── apps/
│   ├── compose/               # KMP app module (entry point for front end app)
│   │   ├── src/                     
│   │   │   ├── commonMain/    # Shared code across platforms
│   │   │   ├── androidMain/   # Android-specific code
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
- `di-kodein`: Dependency injection system (Kodein DI), [more details](./features/di-kodein/README.md)
- `theme`: App theme system, [more details](./features/theme/README.md)
- `tracker`: Running tracker with smoothed GPS, speed, and pace, [more details](./features/tracker/README.md)
- `utils`: Multiplatform utilities (typed result, SafeContinuation, etc.), [more details](./features/utils/README.md)

## Visibility Rules
- Access modifiers are part of the frontend architecture, not a style preference.
- `public` is used only for the module API and for instantiating internal platform-split components from outside the module.
- `internal` is used for everything that must stay inside the module boundary, even if the owning type is `public`.
- If a type is `public` only to allow platform-specific construction or DI wiring, this should be reflected in the module documentation.
