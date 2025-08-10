# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

OpenKarotz-Android is a modernized Android application for controlling Karotz smart devices (rabbit-shaped toys) running OpenKarotz firmware. This is a 2025 modernization of Olivier Bagot's original 2014 app, now supporting multiple devices and contemporary Android development practices.

## Essential Commands

### Build & Development
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build
./gradlew clean

# Install debug build to connected device
./gradlew installDebug

# Run tests
./gradlew test

# Run instrumentation tests
./gradlew connectedAndroidTest
```

### Code Analysis
```bash
# Check for lint issues
./gradlew lint

# Lint with detailed report
./gradlew lintDebug
```

## Architecture Overview

### Core Structure
- **MVVM Architecture** with AndroidX ViewModel and LiveData
- **Multi-device support** via Room database for storing Karotz configurations
- **Navigation Component** for fragment-based UI navigation
- **Repository Pattern** for data abstraction layer
- **Retrofit + OkHttp** for network communication with Karotz devices

### Key Components

**Activities:**
- `MainActivity` - Main entry with navigation drawer
- `DeviceManagementActivity` - Multi-device configuration
- `AddEditDeviceActivity` - Device setup forms

**Fragments:**
- `HomeFragment` - Dashboard
- `ColorFragment` - LED color control
- `EarsFragment` - Ear movement control  
- `RadioFragment` - Internet radio (80+ French stations)
- `SystemFragment` - Device status monitoring

**Data Layer:**
- `KarotzDevice` entity - Device configuration storage
- `KarotzDeviceDao` - Database operations
- `KarotzDeviceRepository` - Data access abstraction
- `AppDatabase` - Room database instance

**Network Layer:**
- `OpenKarotzApi` - Retrofit interface for device communication
- `IKarotz` - Device communication abstraction
- `ServiceGenerator` - Network service factory

### Package Structure
```
com.github.wulfaz.android.openkarotz/
├── activity/          # Activities
├── fragment/          # UI fragments  
├── adapter/           # RecyclerView adapters
├── api/              # Network API interfaces
├── database/         # Room database components
├── model/            # Data models and responses
├── repository/       # Data repositories
├── service/          # Background services
├── task/             # AsyncTask implementations
├── utils/            # Utility classes
└── widget/           # Custom UI components
```

## Development Context

### Target Platform
- **Min SDK:** Android 7.0 (API 24)
- **Target SDK:** Android 14 (API 36)
- **Language:** Java with Kotlin support
- **Architecture:** MVVM with AndroidX components

### Key Dependencies
- **Material Design 3** for modern UI
- **Room Database** for local device storage
- **Retrofit 3.0** for REST API communication
- **Navigation Component** for fragment navigation
- **Glide** for image loading
- **Coroutines** for async operations

### Network Communication
- **Default Port:** 80 for Karotz device communication
- **Protocol:** HTTP REST API to OpenKarotz firmware
- **Multi-device:** Supports multiple Karotz devices on local network
- **Cleartext Traffic:** Enabled for local device communication

### Build Configuration
- **Gradle:** Modern Android Gradle Plugin
- **ViewBinding & DataBinding:** Both enabled
- **ProGuard:** Disabled in debug builds
- **Kotlin Parcelize:** Enabled for model serialization

## Important Notes

### Device Communication
All Karotz communication happens over local network HTTP REST API. The app stores multiple device configurations in Room database and allows switching between devices. Each device has status monitoring for online/offline state.

### Radio Feature
The app includes 80+ pre-configured French radio stations stored in assets. Radio streaming is handled through the Karotz device's built-in capabilities.

### Testing Strategy
- Unit tests with JUnit 4
- Instrumentation tests with Espresso
- AsyncTask testing for network operations

### Multi-Device Architecture
The modernization focuses heavily on multi-device support - users can configure and manage multiple Karotz devices, switch between them, and monitor their status in real-time through the Room database and LiveData reactive updates.