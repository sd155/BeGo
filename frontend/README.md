# BeGo Frontend

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
│   ├── theme/                    # App theme module
│   └── di-kodein/                # Shared DI module (Kodein DI)
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

- App theme system, [more details](./frontend/features/theme/README.md)
- Dependency injection system (Kodein DI), [more details](./frontend/features/di-kodein/README.md)