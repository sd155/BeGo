# App Theme Module

The theme module provides a comprehensive design system for the Bego application. It implements a Material Design-based theming system using Jetpack Compose.

## Key Components

### BegoTheme composable
   - Main theme composable that provides consistent design across the app
   - Supports both light and dark themes
   - Adapts to different screen sizes (Small and Medium)

### BegoTheme object
Provides access to theme values
```kotlin
//use it to get theme colors
BegoTheme.palette
//use it to get theme text styles
BegoTheme.typography
//use it to get theme shapes
BegoTheme.shapes
//use it to get theme size styles
BegoTheme.sizes
```

### Design System Elements
- **Colors**: Defines the app's color palette with primary, secondary, accent, and semantic colors
- **Typography**: Provides text styles for headers, labels, and body text
- **Shapes**: Defines shapes for UI elements like buttons
- **Sizes**: Manages consistent dimensions for icons, padding, and other UI elements

### **UI Components**
- **BegoFilledButton**: A pre-styled button component following the design system
- More components can be added as needed