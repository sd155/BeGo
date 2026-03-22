# App Theme Module

Comprehensive design system. It implements a Material Design-based theming system using Compose Multiplatform.  
Back to the [BeGo frontend application](../../README.md)

## Features
- **Dark mode**: Supports both light and dark themes.
- **Screen sizes**: Adapts to different screen sizes (Small and Medium).
- **Design system**:
  - **Colors**: Defines the app's color palette with primary, secondary, accent, and semantic colors
  - **Typography**: Provides text styles for headers, labels, and body text
  - **Shapes**: Defines shapes for UI elements like buttons
  - **Sizes**: Manages consistent dimensions for icons, padding, and other UI elements
  - **PlatformIcons**: Provides platform-specific icons. This allows UI to use native icons for each platform.
- **Preview Utilities**: Includes preview utilities for Android Studio (Android target)

## API
- Common 
  - enum `DeviceScreen`
  - class `BegoPalette`
    - `.primary: Color`
    - `.secondary: Color`
    - `.background: Color`
    - `.accent: Color`
    - `.onAccent: Color`
    - `.warning: Color`
  - class `BegoTypography`
    - `.header: TextStyle`
    - `.label: TextStyle`
    - `.bodyL: TextStyle`
    - `.bodyM: TextStyle`
  - class `BegoShapes`
    - `.button: Shape`
  - class `BegoSizes`
    - `.screen: DeviceScreen`
    - `.icon: Dp`
    - `.buttonWidth: Dp`
    - `.paddingVertical: Dp`
    - `.paddingHorizontal: Dp`
    - `.contentVerticalPadding: Dp`
  - interface `PlatformIcons`
    - `.check(): ImageVector`
    - `.dropDown(): ImageVector`
  - composable `BegoTheme.palette: BegoPalette`
  - composable `BegoTheme.typography: BegoTypography`
  - composable `BegoTheme.shapes: BegoShapes`
  - composable `BegoTheme.sizes: BegoSizes`
  - composable `BegoTheme.platformIcons: PlatformIcons`
  - composable `BegoTheme(..): Unit`  
  - composable `BegoAccentFilledButton(..): Unit`
  - composable `BegoWarningFilledButton(..): Unit`
  - composable `BegoPrimaryFilledButton(..): Unit`
  - composable `BegoHeaderText(..): Unit`
  - composable `BegoBodyLargeText(..): Unit`
  - composable `BegoDropDown(..): Unit`
  - data class `BegoDropDownItemData`
- Android
  - composable `screenSize(): DeviceScreen`
  - `AndroidPlatformIcons` : PlatformIcons
- Android.debug
  - `LOCALE_EN`
  - `LOCALE_RU`
  - `PHONE_LAND_SPEC`
  - `PHONE_PORT_SPEC`
  - composable `ThemedPreview(..): Unit`

#### Usage
Access to theme values:
```kotlin
// Get theme colors
BegoTheme.palette
// Get theme text styles
BegoTheme.typography
// Get theme shapes
BegoTheme.shapes
// Get theme size styles
BegoTheme.sizes
```
