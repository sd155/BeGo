# Project rules and policies

## Git policy

- Default and release branch is **main**.
- RC (release candidate) branche is **dev**.
- Use [Conventional Commits](https://www.conventionalcommits.org) specification for writing commit messages in a structured and consistent way.

## Project scheme
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
