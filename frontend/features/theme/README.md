# App Theme Module

The theme module provides a comprehensive design system for the [Bego frontend application](../../README.md). It implements a Material Design-based theming system using Jetpack Compose.

## Key Components

### BegoTheme composable
   - Main theme composable that provides consistent design across the app
   - Supports both light and dark themes
   - Adapts to different screen sizes (Small and Medium)

### BegoTheme object
Provides access to theme values:
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

### UI Components
- **BegoAccentFilledButton**: A filled button with accent color for primary actions
- **BegoWarningFilledButton**: A filled button with warning color for destructive actions
- **BegoPrimaryFilledButton**: A filled button with primary color for standard actions
- **BegoHeaderText**: Text component for main headings and titles
- **BegoBodyLargeText**: Text component for large body text
- **BegoDropDown**: A composable dropdown menu for selecting from a list of items. Accepts a list of [BegoDropDownItemData], displays the selected item, and invokes a callback when an item is selected.

### Design System Elements
- **Colors**: Defines the app's color palette with primary, secondary, accent, and semantic colors
- **Typography**: Provides text styles for headers, labels, and body text
- **Shapes**: Defines shapes for UI elements like buttons
- **Sizes**: Manages consistent dimensions for icons, padding, and other UI elements

### Preview Utilities
The module includes preview utilities for Android Studio (Android target):
- Device specifications for different screen sizes and orientations
- Locale constants for internationalization testing
- `ThemedPreview` composable for previewing with the Bego theme applied