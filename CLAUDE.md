# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
This is an Android health tracking application called "myaku_rismu" built with Kotlin and Jetpack Compose. The app integrates with Android Health Connect to track health metrics like heart rate, steps, distance, calories burned, and sleep data.

## Build System and Common Commands

### Build and Development
```bash
# Build the project
./gradlew build

# Install debug APK
./gradlew installDebug

# Run tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean
```

### Dependency Management
- Uses Gradle Version Catalog (`gradle/libs.versions.toml`)
- Dependencies are managed through the `libs.versions.toml` file
- Kotlin version: 2.0.21
- Compose BOM version: 2024.09.00
- Min SDK: 28, Target SDK: 35

## Architecture

### Overall Structure
The project follows Clean Architecture principles with clear separation of concerns:

```
app/src/main/java/com/example/myaku_rismu/
├── core/              # App-wide components and utilities
├── data/              # Data layer (DataSources, Repositories)
├── domain/            # Domain layer (Models, Use Cases)
├── feature/           # Feature-specific UI and ViewModels
└── ui/                # Common UI components and theming
```

### Key Architectural Patterns

#### Dependency Injection
- Uses Dagger Hilt for dependency injection
- Application class annotated with `@HiltAndroidApp`
- MainActivity annotated with `@AndroidEntryPoint`
- DI modules located in `data/di/`

#### Navigation
- Uses Jetpack Navigation Compose with type-safe navigation
- Route definitions in `core/navigation/route.kt` using `@Serializable` data classes
- Navigation setup in `core/navigation/AppNavigation.kt`

#### State Management
- MVVM pattern with ViewModels for each feature
- UI state classes follow the pattern: `[Feature]State.kt`
- UI events follow the pattern: `[Feature]UiEvent.kt`

#### Feature Structure
Each feature follows this consistent structure:
```
feature/[featureName]/
├── [FeatureName]Screen.kt      # Composable UI
├── [FeatureName]ViewModel.kt   # ViewModel with business logic
├── [FeatureName]State.kt       # UI state data class
├── [FeatureName]UiEvent.kt     # UI event sealed class
└── [FeatureName]Navigation.kt  # Navigation extension
```

### Health Connect Integration
- Primary feature integrates with Android Health Connect
- Permissions defined in AndroidManifest.xml for health data access
- Health Connect availability checking and permission management
- Data types: Heart Rate, Steps, Distance, Calories, Sleep
- Custom data models in `domain/model/HealthConnectModels.kt`

### UI and Theming
- Jetpack Compose with Material 3
- Custom theming system with `LocalCustomTheme`
- Dark/Light theme support
- Vico library for charts and data visualization
- Portrait orientation locked

## Important Files

### Core Application Files
- `core/base/Application.kt` - Hilt application entry point
- `core/base/MainActivity.kt` - Main activity with edge-to-edge UI
- `core/base/MainAppScreen.kt` - Main app composable with navigation

### Configuration Files
- `build.gradle.kts` - Root build configuration
- `app/build.gradle.kts` - App module build configuration with dependencies
- `gradle/libs.versions.toml` - Dependency version catalog
- `AndroidManifest.xml` - App manifest with Health Connect permissions

### Data Layer
- `data/datasource/HealthConnectDataSource.kt` - Health Connect data interface
- `data/datasource/HealthConnectDataSourceImpl.kt` - Health Connect implementation
- `data/di/DataSourceModule.kt` - Dependency injection for data sources

### Domain Layer
- `domain/model/HealthConnectModels.kt` - Health Connect domain models
- `domain/useCase/HealthConnectPermissionUseCase.kt` - Permission use cases

## Development Notes

### Testing
- Unit tests in `src/test/`
- Instrumented tests in `src/androidTest/`
- Test runner: AndroidJUnitRunner

### Health Connect Requirements
- Requires Health Connect app to be installed on device
- Handles availability checking and update prompts
- Permission rationale activity for health data access
- Uses Health Connect Client API version 1.1.0-rc02

### Code Conventions
- Kotlin with official code style
- Compose UI with Material 3 design
- Clean Architecture separation
- Sealed classes for events and states
- Data classes for state management