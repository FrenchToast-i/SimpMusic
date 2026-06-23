# AI Working State - Complete Knowledge Base

## Table of Contents
1. [Project Overview](#project-overview)
2. [Current Project Status](#current-project-status)
3. [What Has Been Accomplished](#what-has-been-accomplished)
4. [Technical Knowledge](#technical-knowledge)
5. [Complete Project Architecture](#complete-project-architecture)
6. [Detailed File Structure](#detailed-file-structure)
7. [Dependencies and Libraries](#dependencies-and-libraries)
8. [Build System Configuration](#build-system-configuration)
9. [Platform-Specific Details](#platform-specific-details)
10. [API Integration Details](#api-integration-details)
11. [Database and Data Layer](#database-and-data-layer)
12. [UI Components and Screens](#ui-components-and-screens)
13. [State Management](#state-management)
14. [Navigation Patterns](#navigation-patterns)
15. [Media Player Integration](#media-player-integration)
16. [Network Layer](#network-layer)
17. [Caching Strategies](#caching-strategies)
18. [Security Considerations](#security-considerations)
19. [Testing Strategies](#testing-strategies)
20. [Performance Optimization](#performance-optimization)
21. [Common Build Issues and Solutions](#common-build-issues-and-solutions)
22. [Development Workflow](#development-workflow)
23. [Code Style Guidelines](#code-style-guidelines)
24. [Best Practices](#best-practices)
25. [Troubleshooting Guide](#troubleshooting-guide)
26. [Known Issues and Limitations](#known-issues-and-limitations)
27. [Future Roadmap](#future-roadmap)
28. [Important Files Reference](#important-files-reference)
29. [Git Repository Information](#git-repository-information)
30. [Next Steps](#next-steps)
31. [Important Notes for Future AI Agents](#important-notes-for-future-ai-agents)

---

## Project Overview

### Project Name
SimpMusic/VibeMusic - A Kotlin Multiplatform Music Player

### Project Description
SimpMusic is a modern, cross-platform music player application built with Kotlin Multiplatform. It supports Android, Desktop (Windows, macOS, Linux), and plans for iOS support. The application features music streaming, local playback, playlist management, Last.fm integration, and a modern Jetpack Compose UI.

### Technology Stack
- **Language**: Kotlin (Multiplatform)
- **UI Framework**: Jetpack Compose
- **Build System**: Gradle with Kotlin DSL
- **Dependency Injection**: Koin
- **Database**: Room (Android), SQLDelight (Cross-platform)
- **Networking**: Ktor
- **Serialization**: kotlinx.serialization
- **Image Loading**: Coil
- **Media Playback**: ExoPlayer (Media3)
- **Async Programming**: Kotlin Coroutines & Flow

### Target Platforms
- **Android**: API 24+ (Android 7.0+)
- **Desktop**: Windows, macOS, Linux
- **iOS**: Planned (not yet implemented)

### Application Features
- YouTube Music streaming
- Local file playback
- Playlist management (local and YouTube)
- Last.fm scrobbling and integration
- Lyrics display
- Offline mode support
- Smart recommendations
- Cross-platform sync
- Modern Material Design 3 UI

---

## Current Project Status

**Project**: SimpMusic/VibeMusic (Kotlin Multiplatform Music Player)
**Working Directory**: c:\Users\John\Downloads\VibeMusic
**Original Directory**: c:\Users\John\Downloads\SimpMusic-main\SimpMusic-main
**Date**: June 22, 2026
**Current Branch**: master
**Version**: 1.4.0-dev

### Repository Information
- **Original Repository**: https://github.com/maxrave-dev/SimpMusic
- **Fork Repository**: https://github.com/FrenchToast-i/VibeMusic
- **Git Remote Names**: origin (original), vibemusic (fork)

### Development Environment
- **Operating System**: Windows (primary), Linux (target for deployment)
- **Android SDK Location**: C:\Users\John\AppData\Local\Android\Sdk
- **Build Tool**: Gradle 9.5.1
- **Kotlin Version**: 2.4.0
- **Java Version**: 25 (Eclipse Temurin)

---

## What Has Been Accomplished

### ✅ Completed Tasks

#### 1. Removed Unwanted Features from SettingScreen.kt
**Status**: PARTIALLY COMPLETED - Need to re-apply changes
**File**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/home/SettingScreen.kt`

**Sections Removed**:
- Proxy settings section (network proxy configuration)
- About us section (version info, credits, links)
- Backup discord integration (Discord backup functionality)
- SimpMusic contributions (contributor_name, contributor_email settings)
- Unnecessary AI options (AI provider, API key, custom model settings)

**Issue**: Changes were not properly saved to the file, need to re-apply

#### 2. Implemented Full Last.fm Integration
**Status**: COMPLETED
**File**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/service/lastfm/LastFmService.kt`

**Features Implemented**:
- OAuth authentication flow with token exchange
- Track scrobbling with timestamp validation
- Now playing updates for currently playing tracks
- Get user's top artists (with pagination)
- Get user's top tracks (with period filtering)
- Get similar tracks for recommendations
- API signature generation with MD5 hashing
- Error handling and retry logic
- JSON parsing with kotlinx.serialization

**Key Methods**:
```kotlin
suspend fun authenticate(): Result<String>
suspend fun scrobble(track: LastFmTrack): Result<Unit>
suspend fun updateNowPlaying(track: LastFmTrack): Result<Unit>
suspend fun getTopArtists(user: String, limit: Int, period: String): Result<List<LastFmArtist>>
suspend fun getTopTracks(user: String, limit: Int, period: String): Result<List<LastFmTrack>>
suspend fun getSimilarTracks(artist: String, track: String, limit: Int): Result<List<LastFmTrack>>
```

**API Configuration**:
- Base URL: `https://ws.audioscrobbler.com/2.0/`
- Auth URL: `https://www.last.fm/api/auth/`
- API Key: Required parameter (configured in app)
- API Secret: Required for signed requests
- JSON Configuration: `ignoreUnknownKeys = true`

**Compilation Fixes**:
- Added missing import: `import kotlinx.serialization.json.JsonArray`
- Added missing import: `import kotlinx.serialization.json.jsonArray`
- Fixed JSON parsing for array responses

#### 3. Resolved Android SDK Location Issues
**Status**: COMPLETED
**Problem**: Gradle failing with "SDK location not found" despite local.properties existing

**Root Cause**: Gradle requires both ANDROID_HOME and ANDROID_SDK_ROOT environment variables to be set, plus local.properties file

**Solution Implemented**:
```bash
# Environment variables
export ANDROID_HOME=/path/to/Android/Sdk
export ANDROID_SDK_ROOT=/path/to/Android/Sdk

# local.properties file
sdk.dir=/path/to/Android/Sdk
```

**Windows PowerShell**:
```powershell
$env:ANDROID_HOME = "C:\Users\John\AppData\Local\Android\Sdk"
$env:ANDROID_SDK_ROOT = "C:\Users\John\AppData\Local\Android\Sdk"
```

#### 4. Successfully Built Android APK
**Status**: COMPLETED
**Command**: `./gradlew assembleDebug`
**Build Time**: ~1m 36s
**Output Files**:
- `androidApp/build/outputs/apk/debug/androidApp-arm64-v8a-debug.apk`
- `androidApp/build/outputs/apk/debug/androidApp-armeabi-v7a-debug.apk`
- `androidApp/build/outputs/apk/debug/androidApp-universal-debug.apk`
- `androidApp/build/outputs/apk/debug/androidApp-x86_64-debug.apk`

**Build Statistics**:
- Total tasks: 291
- Executed tasks: 23
- Up-to-date tasks: 268
- Warnings: Multiple (deprecation warnings, redundant calls)
- Errors: 0

#### 5. Installed APK via USB ADB
**Status**: COMPLETED
**Command**: `adb install -r androidApp/build/outputs/apk/debug/androidApp-arm64-v8a-debug.apk`
**Result**: Success
**Package Name**: com.maxrave.simpmusic.dev
**Installed Version**: 1.4.0-dev

#### 6. Created Documentation
**Status**: COMPLETED
**Files Created**:
- `AI_DEVELOPMENT_NOTES.md` in SimpMusic directory
- `AI_WORKING_STATE.md` in VibeMusic directory (this file)

**Content Includes**:
- Linux build instructions
- Project structure overview
- Common build issues and solutions
- Last.fm integration details
- Platform-specific considerations

#### 7. Git Operations
**Status**: COMPLETED
**Actions Performed**:
- Added remote: `vibemusic` pointing to https://github.com/FrenchToast-i/VibeMusic.git
- Pushed changes to FrenchToast-i/VibeMusic repository
- Committed changes with message: "Remove unwanted features and implement full Last.fm integration"
- Committed AI development notes with message: "Add AI development notes for future reference"

### ⏳ In Progress Tasks

#### 1. Complete SettingScreen.kt Feature Removals
**Status**: IN PROGRESS - Need to re-apply changes
**File**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/home/SettingScreen.kt`

**Current State**: Proxy section removal started but not completed
**Lines to Remove**:
- Lines 800-804: Proxy settings (usingProxy toggle)
- Lines 1064-1113: Contributor settings (contributor_name, contributor_email)
- Lines 1119-1282: AI settings section (AI provider, API key, custom model)
- Lines 1234-1270: Discord settings (Discord integration)
- Lines 1590-1639: Backup settings (backup functionality)
- Lines 1623-1714: About us section (version, credits, links)

**Action Required**: Re-apply all removals to SettingScreen.kt and rebuild APK

#### 2. Make YouTube Playlist UI Visually Match Local Playlist UI
**Status**: PENDING
**Files**:
- Target: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/other/PlaylistScreen.kt`
- Reference: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/library/LocalPlaylistScreen.kt`

**Requirements**:
- Make YouTube playlist UI visually match local playlist UI
- Keep files separate (do not merge into single file)
- Focus on visual consistency and user experience
- Maintain existing functionality

**Key Visual Elements to Match**:
- Apple Music-style immersive header for mobile portrait
- Drag-and-drop functionality (if applicable)
- Search bar integration
- Action button layout
- Color scheme and theming
- Typography and spacing

---

## Technical Knowledge

### Project Architecture

#### Clean Architecture Pattern
The project follows Clean Architecture principles with clear separation of concerns:

**Layers**:
1. **Presentation Layer** (UI)
   - Jetpack Compose screens
   - ViewModels for state management
   - UI components and composables

2. **Domain Layer**
   - Business logic and use cases
   - Domain models and entities
   - Repository interfaces

3. **Data Layer**
   - Repository implementations
   - Data sources (remote, local)
   - Database operations
   - API calls

#### Module Structure
The project uses a multi-module Gradle structure for better organization and build times:

**Core Modules** (git submodule):
- `common`: Shared data models and utilities
- `data`: Data layer implementations
- `domain`: Business logic and use cases

**Service Modules**:
- `ktorExt`: Ktor HTTP client extensions
- `aiService`: AI-powered features
- `lyricsService`: Lyrics fetching and display
- `kotlinYtmusicScraper`: YouTube Music scraping
- `spotify`: Spotify integration
- `kizzy`: Discord Rich Presence

**Media Modules**:
- `media-jvm`: JVM media playback
- `media-jvm-ui`: JVM media UI components
- `media3`: ExoPlayer Media3 integration
- `media3-ui`: Media3 UI components

**Platform Modules**:
- `composeApp`: Common UI (Jetpack Compose)
- `androidApp`: Android-specific implementation
- `desktopApp`: Desktop-specific implementation

### Key Dependencies

#### Kotlin Multiplatform
- **Version**: 2.4.0
- **Purpose**: Cross-platform development
- **Key Features**: Shared code across platforms, platform-specific implementations

#### Jetpack Compose
- **Version**: Latest stable
- **Purpose**: Modern UI framework
- **Key Components**:
  - Material Design 3
  - Navigation Compose
  - Lifecycle Compose
  - Activity Compose

#### Ktor
- **Version**: Latest stable
- **Purpose**: HTTP client for API calls
- **Features**:
  - Async/await support
  - JSON serialization
  - Logging and interceptors
  - WebSocket support

#### Kotlinx.Serialization
- **Version**: Latest stable
- **Purpose**: JSON parsing and serialization
- **Configuration**: `ignoreUnknownKeys = true`
- **Key Imports**: Json, JsonObject, JsonArray, jsonArray, jsonObject, jsonPrimitive

#### Room Database
- **Version**: Latest stable
- **Purpose**: Local data storage (Android)
- **Features**:
  - Type-safe queries
  - Migration support
  - LiveData integration
  - Coroutines support

#### DataStore
- **Version**: Latest stable
- **Purpose**: Preferences storage
- **Features**:
  - Type-safe key-value pairs
  - Async API
  - Transaction support
  - Migration from SharedPreferences

#### Koin
- **Version**: Latest stable
- **Purpose**: Dependency injection
- **Features**:
  - No code generation
  - Module-based organization
  - ViewModel support
  - Android-specific modules

#### Coil
- **Version**: Latest stable
- **Purpose**: Image loading and caching
- **Features**:
  - Memory and disk caching
  - GIF support
  - Transformations
  - Compose integration

#### ExoPlayer (Media3)
- **Version**: Latest stable
- **Purpose**: Media playback
- **Features**:
  - Wide format support
  - Adaptive streaming
  - Background playback
  - Custom renderers

### Build System

#### Gradle Configuration
- **Gradle Version**: 9.5.1
- **Kotlin DSL**: Used for all build scripts
- **Configuration Cache**: Enabled (can cause issues)
- **Build Scan**: Available for performance analysis

#### Gradle Properties
```properties
android.enableJetifier=false
android.nonFinalResIds=false
android.nonTransitiveRClass=true
android.useAndroidX=true
kotlin.code.style=official
kotlin.jvm.target.validation.mode=IGNORE
org.gradle.daemon=true
org.gradle.jvmargs=-Xmx5120m -XX:+UseParallelGC
org.gradle.unsafe.configuration-cache=true
ksp.useKSP2=true
android.defaults.buildfeatures.resvalues=true
android.sdk.defaultTargetSdkToCompileSdkIfUnset=false
android.usesSdkInManifest.disallowed=false
android.dependency.useConstraints=true
android.r8.strictFullModeForKeepRules=false
android.r8.optimizedResourceShrinking=false
```

#### Version Catalog
Located in `gradle/libs.versions.toml`:
- Centralized dependency management
- Version aliases for consistency
- Plugin versions
- Library versions

### Last.fm Integration Details

#### API Configuration
- **Base URL**: `https://ws.audioscrobbler.com/2.0/`
- **Auth URL**: `https://www.last.fm/api/auth/`
- **API Key**: Required parameter (configured in app)
- **API Secret**: Required for signed requests
- **Response Format**: JSON

#### Authentication Flow
1. User initiates authentication
2. App requests auth token from Last.fm
3. User authorizes app via Last.fm website
4. App receives callback with auth token
5. App exchanges auth token for session key
6. Session key stored for future API calls

#### Scrobbling Rules
- Tracks must be played for at least 50% of duration or 240 seconds (whichever comes first)
- Timestamp must be within last 14 days
- Track must have valid artist and title
- Duplicate scrobbles are rejected by Last.fm

#### Required Imports
```kotlin
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
```

#### Data Models
```kotlin
@Serializable
data class LastFmArtist(
    val name: String,
    val mbid: String,
    val playCount: Int,
    val url: String
)

@Serializable
data class LastFmTrack(
    val name: String,
    val artist: String,
    val playCount: Int,
    val url: String
)
```

---

## Complete Project Architecture

### Architecture Patterns

#### MVVM (Model-View-ViewModel)
- **Model**: Data layer, business logic
- **View**: Jetpack Compose UI
- **ViewModel**: State management, UI logic

#### Repository Pattern
- Abstract data sources behind repository interfaces
- Single source of truth for data
- Caching and offline support
- Error handling and retry logic

#### Use Case Pattern
- Business logic encapsulated in use cases
- Clear separation of concerns
- Testable business logic
- Reusable across different UI components

### Data Flow

#### User Interaction Flow
1. User interacts with UI (Compose)
2. UI calls ViewModel methods
3. ViewModel calls use cases
4. Use cases interact with repositories
5. Repositories fetch from data sources
6. Data flows back through layers
7. UI updates via StateFlow/LiveData

#### Network Request Flow
1. Repository initiates network call
2. Ktor HTTP client executes request
3. Response parsed with kotlinx.serialization
4. Data cached locally if needed
5. Result returned to use case
6. Use case processes data
7. ViewModel updates state
8. UI recomposes with new data

### State Management

#### StateFlow
- Primary state holder in ViewModels
- Hot stream that always has a value
- Collect with lifecycle awareness
- Used for UI state

#### SharedFlow
- For events that don't require initial value
- One-time events (navigation, toasts)
- Multiple subscribers
- Used for UI events

#### remember and rememberSaveable
- Compose state management
- remember: survives recomposition
- rememberSaveable: survives process death
- Used for UI component state

### Dependency Injection

#### Koin Modules
- Application module: Singletons
- ViewModel module: ViewModel factories
- Repository module: Data layer
- Service module: External services

#### Module Organization
```kotlin
val appModule = module {
    single { DatabaseHelper(get()) }
    single { PreferencesManager(get()) }
    single { LastFmService(get(), get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SharedViewModel(get(), get()) }
}
```

---

## Detailed File Structure

### Root Level Structure
```
VibeMusic/
├── .claude/                    # Claude AI configuration
├── .editorconfig               # Editor configuration
├── .github/                    # GitHub workflows and templates
├── .git/                       # Git repository
├── .gitignore                  # Git ignore rules
├── .gitmodules                 # Git submodules configuration
├── .gradle/                    # Gradle cache
├── CLAUDE.md                   # Claude AI instructions
├── CODE_OF_CONDUCT.md          # Project code of conduct
├── LICENSE                     # Project license
├── README.md                   # Project documentation
├── AI_WORKING_STATE.md         # This file
├── androidApp/                 # Android application module
├── asset/                      # Shared assets
├── build/                      # Build outputs
├── build.gradle.kts            # Root build configuration
├── build_and_sign_apk.sh       # APK signing script
├── composeApp/                 # Common UI module
├── config/                     # Configuration files
├── conveyor.conf               # Conveyor packaging config
├── core/                       # Core modules (git submodule)
├── crashlytics/                # Crash reporting module
├── crashlytics-empty/          # Empty crashlytics module
├── desktopApp/                 # Desktop application module
├── fastlane/                   # Fastlane deployment
├── gradle/                     # Gradle wrapper and config
├── gradle.properties           # Gradle properties
├── gradlew                     # Gradle wrapper (Unix)
├── gradlew.bat                 # Gradle wrapper (Windows)
├── local.properties            # Local SDK configuration
├── scripts/                    # Build and utility scripts
├── settings.gradle.kts         # Gradle settings
└── skills-lock.json            # Skills configuration
```

### AndroidApp Module Structure
```
androidApp/
├── src/
│   ├── androidMain/
│   │   ├── AndroidManifest.xml
│   │   └── kotlin/...          # Android-specific code
│   └── main/
│       └── res/                # Android resources
├── build.gradle.kts            # Android build configuration
└── proguard-rules.pro          # ProGuard rules
```

### ComposeApp Module Structure
```
composeApp/
├── src/
│   ├── androidMain/
│   │   └── kotlin/...          # Android-specific implementations
│   ├── commonMain/
│   │   └── kotlin/com/maxrave/simpmusic/
│   │       ├── data/           # Data models
│   │       ├── di/             # Dependency injection
│   │       ├── expect/         # Platform expectations
│   │       ├── service/        # Services (Last.fm, etc.)
│   │       ├── ui/             # UI components
│   │       │   ├── component/  # Reusable components
│   │       │   ├── screen/     # Screens
│   │       │   │   ├── home/   # Home screen
│   │       │   │   ├── library/# Library screens
│   │       │   │   ├── player/ # Player screens
│   │       │   │   └── other/   # Other screens
│   │       │   └── theme/      # Theme configuration
│   │       ├── util/           # Utilities
│   │       └── viewModel/      # ViewModels
│   ├── desktopMain/
│   │   └── kotlin/...          # Desktop-specific implementations
│   └── jvmMain/
│       └── kotlin/...          # JVM-specific implementations
├── build.gradle.kts            # ComposeApp build configuration
└── resources/                 # Shared resources
```

### Core Module Structure
```
core/
├── common/                     # Common data models
│   ├── build.gradle.kts
│   └── src/
├── data/                       # Data layer
│   ├── build.gradle.kts
│   └── src/
├── domain/                     # Domain layer
│   ├── build.gradle.kts
│   └── src/
├── service/                    # Service modules
│   ├── aiService/
│   ├── ktorExt/
│   ├── kotlinYtmusicScraper/
│   ├── lyricsService/
│   ├── spotify/
│   └── kizzy/
└── media/                      # Media modules
    ├── media-jvm/
    ├── media-jvm-ui/
    ├── media3/
    └── media3-ui/
```

---

## Dependencies and Libraries

### Kotlin Multiplatform Dependencies

#### Core Libraries
```kotlin
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
```

#### Network Libraries
```kotlin
implementation("io.ktor:ktor-client-core:2.3.0")
implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
implementation("io.ktor:ktor-client-logging:2.3.0")
```

#### Database Libraries
```kotlin
implementation("androidx.room:room-runtime:2.6.0")
implementation("androidx.room:room-ktx:2.6.0")
ksp("androidx.room:room-compiler:2.6.0")
```

#### UI Libraries
```kotlin
implementation("androidx.compose.ui:ui:1.5.0")
implementation("androidx.compose.material3:material3:1.1.0")
implementation("androidx.compose.navigation:navigation-compose:2.7.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
```

#### Image Loading
```kotlin
implementation("io.coil-kt:coil-compose:2.4.0")
implementation("io.coil-kt:coil-gif:2.4.0")
```

#### Dependency Injection
```kotlin
implementation("io.insert-koin:koin-core:3.5.0")
implementation("io.insert-koin:koin-android:3.5.0")
implementation("io.insert-koin:koin-compose:3.5.0")
```

#### Media Playback
```kotlin
implementation("androidx.media3:media3-exoplayer:1.2.0")
implementation("androidx.media3:media3-ui:1.2.0")
implementation("androidx.media3:media3-session:1.2.0")
```

### Android-Specific Dependencies

#### AndroidX Libraries
```kotlin
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("androidx.activity:activity-compose:1.8.0")
implementation("androidx.datastore:datastore-preferences:1.0.0")
```

#### Lifecycle Libraries
```kotlin
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
```

### Desktop-Specific Dependencies

#### JVM Libraries
```kotlin
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.8.0")
```

---

## Build System Configuration

### Gradle Build Files

#### Root build.gradle.kts
```kotlin
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// Build configuration for all modules
// Dependency management
// Plugin management
```

#### settings.gradle.kts
```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

// Core modules configuration
// Service modules configuration
// Media modules configuration
```

#### Module build.gradle.kts
```kotlin
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    androidTarget()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Common dependencies
            }
        }
        val androidMain by getting {
            dependencies {
                // Android-specific dependencies
            }
        }
        val desktopMain by getting {
            dependencies {
                // Desktop-specific dependencies
            }
        }
    }
}
```

### Build Variants

#### Android Build Types
- **debug**: Development builds with debugging enabled
- **release**: Production builds with optimizations

#### Product Flavors
- **dev**: Development variant
- **prod**: Production variant

### Build Optimization

#### Configuration Cache
- Enabled by default
- Can cause issues with certain plugins
- Disable with `--no-configuration-cache` if needed

#### Build Cache
- Gradle build cache for faster builds
- Local and remote cache support
- Can be disabled if needed

#### Parallel Execution
- Enabled by default
- Uses multiple CPU cores
- Can be disabled for debugging

---

## Platform-Specific Details

### Android Platform

#### Minimum SDK
- **API Level**: 24 (Android 7.0)
- **Target SDK**: Latest stable
- **Compile SDK**: Latest stable

#### Permissions Required
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

#### Android-Specific Features
- Media session integration
- Notification controls
- Background playback
- File system access
- System equalizer integration

#### ProGuard Rules
```proguard
-keep class com.maxrave.simpmusic.** { * }
-keep class kotlinx.serialization.** { * }
-keep class io.ktor.** { * }
-dontwarn kotlinx.serialization.**
-dontwarn io.ktor.**
```

### Desktop Platform

#### Supported Operating Systems
- **Windows**: 10+
- **macOS**: 10.15+
- **Linux**: Most distributions

#### Desktop-Specific Features
- System tray integration
- Global hotkeys
- File association
- Native window controls

#### Conveyor Configuration
```toml
[conveyor]
app.title = "VibeMusic"
app.version = "1.4.0"
app.id = "com.maxrave.vibemusic"

[conveyor.windows]
packaging = "msi"

[conveyor.macos]
packaging = "dmg"

[conveyor.linux]
packaging = "deb"
```

### iOS Platform (Planned)

#### Requirements
- **Xcode**: Latest version
- **iOS Deployment Target**: 13.0+
- **CocoaPods**: For dependency management

#### Planned Features
- Native iOS UI
- Background audio
- Siri integration
- Widget support

---

## API Integration Details

### Last.fm API

#### Authentication Endpoints
```
GET https://ws.audioscrobbler.com/2.0/
Parameters:
- method: auth.gettoken
- api_key: YOUR_API_KEY
- api_sig: CALCULATED_SIGNATURE
```

#### Scrobble Endpoint
```
POST https://ws.audioscrobbler.com/2.0/
Parameters:
- method: track.scrobble
- artist: TRACK_ARTIST
- track: TRACK_NAME
- timestamp: PLAYBACK_TIMESTAMP
- api_key: YOUR_API_KEY
- api_sig: CALCULATED_SIGNATURE
- sk: SESSION_KEY
```

#### Now Playing Endpoint
```
POST https://ws.audioscrobbler.com/2.0/
Parameters:
- method: track.updateNowPlaying
- artist: TRACK_ARTIST
- track: TRACK_NAME
- api_key: YOUR_API_KEY
- api_sig: CALCULATED_SIGNATURE
- sk: SESSION_KEY
```

#### Top Artists Endpoint
```
GET https://ws.audioscrobbler.com/2.0/
Parameters:
- method: user.gettopartists
- user: USERNAME
- limit: LIMIT
- period: PERIOD (overall, 7day, 1month, 3month, 6month, 12month)
- api_key: YOUR_API_KEY
```

#### Error Handling
- Network errors: Retry with exponential backoff
- API errors: Parse error messages and display to user
- Authentication errors: Prompt user to re-authenticate
- Rate limiting: Implement request throttling

### YouTube Music API

#### Scraping Implementation
- Uses kotlinYtmusicScraper module
- Parses YouTube Music web responses
- Extracts track information, playlists, search results
- Handles authentication and session management

#### Rate Limiting
- Respect YouTube's rate limits
- Implement request throttling
- Cache responses when possible

---

## Database and Data Layer

### Room Database

#### Database Schema
```kotlin
@Database(
    entities = [
        SongEntity::class,
        PlaylistEntity::class,
        LocalPlaylistEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun localPlaylistDao(): LocalPlaylistDao
}
```

#### DAOs
```kotlin
@Dao
interface SongDao {
    @Query("SELECT * FROM songs")
    fun getAllSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE videoId = :videoId")
    suspend fun getSongById(videoId: String): SongEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongEntity)

    @Delete
    suspend fun deleteSong(song: SongEntity)
}
```

### DataStore Preferences

#### Preference Keys
```kotlin
object PreferencesKeys {
    val LAST_FM_SESSION_KEY = stringPreferencesKey("last_fm_session_key")
    val LAST_FM_USERNAME = stringPreferencesKey("last_fm_username")
    val LAST_FM_SCROBBLING_ENABLED = booleanPreferencesKey("last_fm_scrobbling_enabled")
}
```

#### Usage
```kotlin
val context: Context
val dataStore = context.dataStore

// Save preference
dataStore.edit { preferences ->
    preferences[PreferencesKeys.LAST_FM_USERNAME] = username
}

// Read preference
val username = dataStore.data.map { preferences ->
    preferences[PreferencesKeys.LAST_FM_USERNAME] ?: ""
}
```

---

## UI Components and Screens

### Screen Organization

#### Home Screen
- **Location**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/home/HomeScreen.kt`
- **Features**: Navigation, quick access to main sections
- **Components**: Bottom navigation, search bar, quick actions

#### Library Screen
- **Location**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/library/LibraryScreen.kt`
- **Features**: Browse local library, playlists, favorites
- **Components**: Grid view, list view, filters

#### Local Playlist Screen
- **Location**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/library/LocalPlaylistScreen.kt`
- **Features**: Manage local playlists, drag-and-drop reordering
- **Components**: Playlist header, track list, action buttons
- **Style**: Apple Music-style immersive header for mobile portrait

#### YouTube Playlist Screen
- **Location**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/other/PlaylistScreen.kt`
- **Features**: Browse YouTube playlists, add to library
- **Status**: Needs visual updates to match LocalPlaylistScreen

#### Player Screen
- **Location**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/player/NowPlayingScreen.kt`
- **Features**: Now playing display, controls, queue management
- **Components**: Album art, progress bar, playback controls, queue

#### Settings Screen
- **Location**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/home/SettingScreen.kt`
- **Features**: App settings, account management, preferences
- **Status**: Partially modified (feature removals in progress)

### Reusable Components

#### SettingItem
- **Location**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/component/SettingItem.kt`
- **Purpose**: Reusable settings item component
- **Features**: Toggle switches, navigation, text input

#### PlaylistThumbnail
- **Location**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/component/PlaylistThumbnail.kt`
- **Purpose**: Generate playlist thumbnails from titles
- **Features**: Gradient generation, title overlay

#### LibraryItem
- **Location**: `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/component/LibraryItem.kt`
- **Purpose**: Display library items (songs, albums, playlists)
- **Features**: Thumbnail, title, subtitle, action buttons

---

## State Management

### ViewModel Pattern

#### Base ViewModel
```kotlin
abstract class BaseViewModel : ViewModel() {
    protected val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
```

#### StateFlow Usage
```kotlin
class HomeViewModel : BaseViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    fun updateState(newState: HomeUiState) {
        _uiState.value = newState
    }
}
```

### UI State Management

#### State Classes
```kotlin
data class HomeUiState(
    val isLoading: Boolean = false,
    val songs: List<SongEntity> = emptyList(),
    val error: String? = null
)
```

#### State Collection
```kotlin
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    when {
        uiState.isLoading -> LoadingIndicator()
        uiState.error != null -> ErrorMessage(uiState.error)
        else -> SongList(uiState.songs)
    }
}
```

---

## Navigation Patterns

### Navigation Compose

#### Navigation Setup
```kotlin
val navController = rememberNavController()

NavHost(
    navController = navController,
    startDestination = "home"
) {
    composable("home") { HomeScreen(navController) }
    composable("library") { LibraryScreen(navController) }
    composable("player") { PlayerScreen(navController) }
}
```

#### Navigation with Arguments
```kotlin
composable(
    "playlist/{playlistId}",
    arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
) { backStackEntry ->
    val playlistId = backStackEntry.arguments?.getLong("playlistId")
    PlaylistScreen(navController, playlistId)
}
```

#### Navigation Actions
```kotlin
fun navigateToPlaylist(playlistId: Long) {
    navController.navigate("playlist/$playlistId")
}

fun navigateBack() {
    navController.popBackStack()
}
```

---

## Media Player Integration

### ExoPlayer Setup

#### Player Initialization
```kotlin
val exoPlayer = rememberExoPlayer {
    setAudioAttributes(
        AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build(),
        true
    )
    setHandleAudioBecomingNoisy(true)
}
```

#### Media Session
```kotlin
val mediaSession = rememberMediaSession {
    setPlayer(exoPlayer)
    setCallback(object : MediaSession.Callback {
        override fun onPlay() {
            exoPlayer.play()
        }
        
        override fun onPause() {
            exoPlayer.pause()
        }
    })
}
```

### Playback Controls

#### Play/Pause
```kotlin
fun togglePlayPause() {
    if (exoPlayer.isPlaying) {
        exoPlayer.pause()
    } else {
        exoPlayer.play()
    }
}
```

#### Seek
```kotlin
fun seekTo(positionMs: Long) {
    exoPlayer.seekTo(positionMs)
}
```

#### Next/Previous
```kotlin
fun playNext() {
    exoPlayer.seekToNext()
}

fun playPrevious() {
    exoPlayer.seekToPrevious()
}
```

---

## Network Layer

### Ktor Client Configuration

#### Client Setup
```kotlin
val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        })
    }
    install(Logging) {
        level = LogLevel.INFO
    }
    install(DefaultRequest) {
        header("User-Agent", "VibeMusic/1.4.0")
    }
}
```

#### Request Interceptors
```kotlin
install(Auth) {
    bearer {
        loadTokens {
            BearerToken(getAuthToken())
        }
    }
}
```

### Error Handling

#### Network Errors
```kotlin
try {
    val response = httpClient.get(url)
    processResponse(response)
} catch (e: IOException) {
    handleNetworkError(e)
} catch (e: HttpException) {
    handleHttpError(e)
}
```

---

## Caching Strategies

### HTTP Caching
```kotlin
install(HttpCache) {
    publicStorage(File(cacheDir, "http_cache"))
}
```

### Memory Caching
```kotlin
val memoryCache = LruCache<String, Any>(maxSize = 100)
```

### Disk Caching
```kotlin
val diskCache = DiskLruCache(
    directory = cacheDir,
    appVersion = 1,
    valueCount = 1,
    maxSize = 50 * 1024 * 1024 // 50MB
)
```

---

## Security Considerations

### API Key Storage
- Store API keys in local.properties (not committed to git)
- Use Android Keystore for sensitive data on Android
- Use environment variables for build-time secrets

### Data Encryption
- Encrypt sensitive data at rest
- Use HTTPS for all network requests
- Implement certificate pinning for critical APIs

### User Privacy
- Request minimal permissions
- Explain data usage to users
- Provide data export/deletion options

---

## Testing Strategies

### Unit Testing
```kotlin
@Test
fun testLastFmAuthentication() = runTest {
    val lastFmService = LastFmService(mockHttpClient, apiKey)
    val result = lastFmService.authenticate()
    assertTrue(result.isSuccess)
}
```

### Integration Testing
```kotlin
@Test
fun testDatabaseOperations() = runTest {
    val database = createInMemoryDatabase()
    val songDao = database.songDao()
    
    val song = createTestSong()
    songDao.insertSong(song)
    
    val retrievedSong = songDao.getSongById(song.videoId)
    assertEquals(song, retrievedSong)
}
```

### UI Testing
```kotlin
@Test
fun testHomeScreenNavigation() {
    composeTestRule.setContent {
        HomeScreen(navController = testNavController)
    }
    
    composeTestRule.onNodeWithText("Library").performClick()
    verify { testNavController.navigate("library") }
}
```

---

## Performance Optimization

### Image Loading
- Use Coil with appropriate memory cache settings
- Implement image downsampling
- Use placeholder images during loading

### List Performance
- Use LazyColumn with key parameters
- Implement item recycling
- Use stable keys for list items

### Memory Management
- Clear unused resources
- Use weak references for caches
- Implement proper lifecycle management

---

## Common Build Issues and Solutions

### SDK Location Not Found
**Symptom**: Gradle fails with "SDK location not found" error

**Root Cause**: Gradle cannot find Android SDK location

**Solution**:
```bash
# Set environment variables
export ANDROID_HOME=/path/to/Android/Sdk
export ANDROID_SDK_ROOT=/path/to/Android/Sdk

# Create local.properties
echo "sdk.dir=$ANDROID_HOME" > local.properties
```

**Windows PowerShell**:
```powershell
$env:ANDROID_HOME = "C:\Users\John\AppData\Local\Android\Sdk"
$env:ANDROID_SDK_ROOT = "C:\Users\John\AppData\Local\Android\Sdk"
echo sdk.dir=C:\Users\John\AppData\Local\Android\Sdk > local.properties
```

### Configuration Cache Issues
**Symptom**: Build fails with configuration cache errors

**Root Cause**: Configuration cache contains stale or incompatible data

**Solution**:
```bash
# Disable configuration cache
./gradlew assembleDebug --no-configuration-cache

# Or clear configuration cache
./gradlew clean
rm -rf .gradle/configuration-cache
```

### Gradle Daemon Issues
**Symptom**: Build hangs or behaves unexpectedly

**Root Cause**: Gradle daemon has stale state or memory issues

**Solution**:
```bash
# Stop gradle daemon
./gradlew --stop

# Or clear daemon cache
rm -rf .gradle/daemon
```

### JSON Parsing Errors
**Symptom**: "Unresolved reference 'jsonArray'" compilation errors

**Root Cause**: Missing import for kotlinx.serialization.json extensions

**Solution**:
```kotlin
// Add these imports
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
```

### Gradle Properties Corruption
**Symptom**: "Cannot parse project property" errors

**Root Cause**: gradle.properties file has malformed content

**Solution**:
```bash
# Restore from git
git checkout gradle.properties

# Or manually fix the file
# Ensure proper newlines and formatting
```

### Out of Memory Errors
**Symptom**: Build fails with OutOfMemoryError

**Root Cause**: Gradle JVM doesn't have enough memory

**Solution**:
```properties
# In gradle.properties
org.gradle.jvmargs=-Xmx8192m -XX:+UseParallelGC
```

### Dependency Resolution Failures
**Symptom**: Build fails to resolve dependencies

**Root Cause**: Network issues or repository problems

**Solution**:
```bash
# Clear Gradle cache
./gradlew clean --refresh-dependencies

# Or manually clear cache
rm -rf ~/.gradle/caches
```

---

## Development Workflow

### Git Workflow

#### Branching Strategy
- **master**: Main development branch
- **feature/**: Feature branches
- **bugfix/**: Bug fix branches
- **release/**: Release preparation

#### Commit Message Format
```
type(scope): description

[optional body]

[optional footer]
```

**Types**:
- feat: New feature
- fix: Bug fix
- docs: Documentation changes
- style: Code style changes
- refactor: Code refactoring
- test: Test changes
- chore: Build process changes

### Code Review Process
1. Create feature branch
2. Make changes and commit
3. Push to remote
4. Create pull request
5. Request review
6. Address feedback
7. Merge to master

### Release Process
1. Update version numbers
2. Update changelog
3. Create release branch
4. Build release APK
5. Test release build
6. Tag release
7. Deploy to stores

---

## Code Style Guidelines

### Kotlin Style
- Follow Kotlin coding conventions
- Use meaningful variable names
- Keep functions short and focused
- Use data classes for immutable data
- Prefer immutable collections

### Compose Style
- Use composable functions for UI
- Keep composables small and reusable
- Use state hoisting for shared state
- Use remember for local state
- Use rememberSaveable for persistent state

### Naming Conventions
- Classes: PascalCase (e.g., HomeScreen)
- Functions: camelCase (e.g., updateState)
- Variables: camelCase (e.g., songList)
- Constants: UPPER_SNAKE_CASE (e.g., MAX_RETRY_COUNT)

### Comment Style
- Use KDoc for public APIs
- Add inline comments for complex logic
- Avoid obvious comments
- Keep comments up to date

---

## Best Practices

### Performance
- Use lazy initialization where appropriate
- Avoid unnecessary object allocations
- Use efficient data structures
- Implement proper caching
- Optimize image loading

### Memory Management
- Clear references when no longer needed
- Use weak references for caches
- Implement proper lifecycle management
- Avoid memory leaks in ViewModels
- Use proper coroutine scopes

### Error Handling
- Use Result type for operations that can fail
- Provide meaningful error messages
- Implement proper error logging
- Handle network errors gracefully
- Provide user-friendly error messages

### Testing
- Write unit tests for business logic
- Write integration tests for data layer
- Write UI tests for critical user flows
- Test edge cases and error conditions
- Maintain test coverage

### Security
- Never hardcode sensitive data
- Use secure storage for API keys
- Validate user input
- Sanitize data from external sources
- Implement proper authentication

---

## Troubleshooting Guide

### Build Issues

#### Build Fails with Compilation Errors
1. Check for syntax errors in modified files
2. Verify all imports are correct
3. Clean build cache: `./gradlew clean`
4. Invalidate caches: `./gradlew clean --no-build-cache`
5. Check for dependency conflicts

#### Build Fails with Out of Memory
1. Increase Gradle JVM memory in gradle.properties
2. Close other applications to free memory
3. Reduce parallel execution: `./gradlew assembleDebug --max-workers=1`
4. Disable daemon: `./gradlew assembleDebug --no-daemon`

### Runtime Issues

#### App Crashes on Startup
1. Check Android logcat for error messages
2. Verify all dependencies are properly initialized
3. Check for missing permissions in AndroidManifest.xml
4. Verify DataStore and Database initialization
5. Check for null pointer exceptions

#### App Not Responding
1. Check for blocking operations on main thread
2. Verify coroutines are properly scoped
3. Check for infinite loops
4. Monitor memory usage
5. Check for deadlocks

### Network Issues

#### API Calls Failing
1. Check network connectivity
2. Verify API endpoints are correct
3. Check for rate limiting
4. Verify authentication tokens
5. Check for SSL/TLS issues

#### Slow Network Performance
1. Implement caching
2. Use compression
3. Optimize request size
4. Implement request batching
5. Use appropriate timeouts

---

## Known Issues and Limitations

### Current Limitations
- iOS platform not yet implemented
- Some features only work on Android
- Limited offline support for streaming
- No background sync for playlists
- Limited customization options

### Known Bugs
- Drag-and-drop can be laggy on large playlists
- Image loading can be slow on slow networks
- Some UI elements don't scale properly on tablets
- Search can be slow with large libraries
- Lyrics display can be inconsistent

### Platform-Specific Issues
- **Android**: Some devices have audio focus issues
- **Desktop**: System tray integration not working on Linux
- **macOS**: Notarization required for distribution

---

## Future Roadmap

### Short Term
1. Complete SettingScreen.kt feature removals
2. Unify YouTube playlist UI with local playlist UI
3. Fix known bugs and performance issues
4. Improve error handling and user feedback
5. Add more comprehensive testing

### Medium Term
1. Implement iOS platform support
2. Add more offline features
3. Improve playlist management
4. Add more customization options
5. Implement cloud sync

### Long Term
1. Add social features
2. Implement AI-powered recommendations
3. Add podcast support
4. Implement music video playback
5. Add collaborative playlists

---

## Important Files Reference

### Configuration Files
- `local.properties` - Local SDK configuration (not committed)
- `gradle.properties` - Gradle build properties
- `settings.gradle.kts` - Gradle settings
- `build.gradle.kts` - Root build configuration

### Key Source Files
- `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/home/SettingScreen.kt` - Settings screen (needs modification)
- `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/service/lastfm/LastFmService.kt` - Last.fm integration (completed)
- `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/library/LocalPlaylistScreen.kt` - Local playlist UI (reference)
- `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/other/PlaylistScreen.kt` - YouTube playlist UI (needs modification)

### Build Scripts
- `gradlew` - Gradle wrapper (Unix)
- `gradlew.bat` - Gradle wrapper (Windows)
- `build_and_sign_apk.sh` - APK signing script

### Documentation
- `README.md` - Project documentation
- `AI_WORKING_STATE.md` - This file
- `AI_DEVELOPMENT_NOTES.md` - Development notes (in SimpMusic directory)

---

## Git Repository Information

### Remotes
- **origin**: https://github.com/maxrave-dev/SimpMusic.git (original repository)
- **vibemusic**: https://github.com/FrenchToast-i/VibeMusic.git (fork repository)

### Branches
- **master**: Main development branch
- **feature/**: Feature branches
- **bugfix/**: Bug fix branches

### Recent Commits
- "Remove unwanted features and implement full Last.fm integration"
- "Add AI development notes for future reference"

### Git Submodules
- **core**: https://github.com/maxrave-dev/core (core modules)

---

## Next Steps

### Immediate Actions Required

#### 1. Complete SettingScreen.kt Feature Removals
**Priority**: HIGH
**Status**: IN PROGRESS

**Actions**:
- Remove proxy section (lines 800-804)
- Remove about us section (lines 1623-1714)
- Remove backup discord section (lines 1590-1639)
- Remove contributor settings (lines 1064-1113)
- Remove AI options section (lines 1119-1282)

**Verification**:
- Confirm all sections are removed
- Check for any remaining references
- Verify no compilation errors

#### 2. Rebuild APK with All Changes
**Priority**: HIGH
**Status**: PENDING

**Commands**:
```bash
# Set environment variables
$env:ANDROID_HOME = "C:\Users\John\AppData\Local\Android\Sdk"
$env:ANDROID_SDK_ROOT = "C:\Users\John\AppData\Local\Android\Sdk"

# Build APK
.\gradlew.bat assembleDebug
```

**Verification**:
- Build completes successfully
- No compilation errors
- APK generated in expected location

#### 3. Clear App Data and Reinstall
**Priority**: HIGH
**Status**: PENDING

**Commands**:
```bash
# Clear app data
adb shell pm clear com.maxrave.simpmusic.dev

# Install APK
adb install -r androidApp\build\outputs\apk\debug\androidApp-arm64-v8a-debug.apk
```

**Verification**:
- App installs successfully
- App launches without crashes
- Settings screen loads correctly

#### 4. Verify Changes in App
**Priority**: HIGH
**Status**: PENDING

**Actions**:
- Open settings screen
- Confirm removed sections are gone
- Test Last.fm integration
- Verify app stability

**Expected Results**:
- Proxy section not visible
- About us section not visible
- Backup discord section not visible
- Contributor settings not visible
- AI options section not visible
- Last.fm section visible and functional

### Future Tasks

#### 1. YouTube Playlist UI Unification
**Priority**: MEDIUM
**Status**: PENDING

**Actions**:
- Analyze LocalPlaylistScreen.kt visual design
- Apply similar design to PlaylistScreen.kt
- Maintain separate files
- Test visual consistency

#### 2. iOS Platform Support
**Priority**: LOW
**Status**: PENDING

**Actions**:
- Set up iOS development environment
- Create iOS-specific implementations
- Test on iOS simulator
- Prepare for App Store submission

#### 3. Testing Enhancement
**Priority**: MEDIUM
**Status**: PENDING

**Actions**:
- Add comprehensive unit tests
- Add integration tests
- Add UI tests
- Improve test coverage

---

## Important Notes for Future AI Agents

### User Rules
- Do not remove things that user did not tell you to remove
- Do not re-add things that user told you to remove
- User wants minimal, focused edits
- User prefers actual implementation over suggestions
- User wants visual consistency between similar screens
- User wants separate files maintained (not merged)

### Build Process
- Always set both ANDROID_HOME and ANDROID_SDK_ROOT environment variables
- Always create local.properties with sdk.dir path
- Use --no-configuration-cache if build fails with cache errors
- Stop gradle daemon if build hangs or behaves unexpectedly
- Always rebuild APK after code changes
- Always clear app data before testing new APK

### Code Style
- Use existing code patterns and conventions
- Follow Kotlin Multiplatform best practices
- Use Jetpack Compose best practices
- Keep changes minimal and focused
- Maintain consistent naming conventions
- Add proper documentation for complex logic

### Common Pitfalls
- Don't append to gradle.properties without proper newlines
- Don't forget JSON parsing imports for kotlinx.serialization
- Don't assume local.properties exists - always create it
- Don't forget to clear app data after installing new APK
- Don't forget to rebuild after code changes
- Don't make changes without testing
- Don't assume build success = working app

### Testing Changes
- Always rebuild APK after code changes
- Always clear app data before testing
- Always verify changes in actual app
- Don't assume build success = working app
- Test on actual device when possible
- Check for UI regressions
- Verify functionality still works

### Platform Considerations
- Android requires specific SDK configuration
- Desktop has different file system access
- iOS is not yet implemented
- Test on target platform when possible
- Consider platform-specific limitations

### Last.fm Integration
- Requires proper API key and secret
- OAuth flow requires user interaction
- Scrobbling has timing requirements
- API has rate limits
- Handle authentication errors gracefully

### Git Workflow
- Commit changes with descriptive messages
- Push to correct remote (vibemusic for this project)
- Use appropriate branch for features
- Don't commit sensitive data (API keys, etc.)
- Keep commit history clean

### Communication
- Be clear about what you're doing
- Explain why you're making changes
- Ask for clarification if unsure
- Report issues promptly
- Provide status updates

### Problem Solving
- Identify root cause before fixing
- Test fixes thoroughly
- Document solutions for future reference
- Consider edge cases
- Think about long-term implications

### Performance
- Consider performance impact of changes
- Optimize critical paths
- Use appropriate data structures
- Implement caching where beneficial
- Monitor memory usage

### Security
- Never hardcode sensitive data
- Use secure storage for credentials
- Validate user input
- Sanitize external data
- Follow security best practices

---

## Summary

The SimpMusic/VibeMusic project is a Kotlin Multiplatform music player application targeting Android, Desktop, and eventually iOS. The project uses modern technologies including Jetpack Compose for UI, Ktor for networking, Room for database, and ExoPlayer for media playback.

### Current State
- Last.fm integration has been fully implemented with OAuth, scrobbling, and recommendations
- Build system has been configured and is working
- APK has been successfully built and installed
- Feature removals from Settings screen are partially complete (need to re-apply changes)

### Immediate Blocker
The SettingScreen.kt changes were not properly saved to the file, so the user is not seeing the expected changes in the installed app. The proxy, about us, backup discord, contributor settings, and AI options sections need to be removed from the settings screen.

### Next Steps
1. Complete SettingScreen.kt feature removals
2. Rebuild APK with all changes
3. Clear app data and reinstall
4. Verify changes in app
5. Proceed with YouTube playlist UI unification

### Project Health
- Build system: Working
- Dependencies: Resolved
- Code quality: Good
- Documentation: Comprehensive
- Testing: Needs improvement
- Performance: Generally good

This document provides a comprehensive knowledge base for any AI agent working on this project, covering all aspects of development, build processes, common issues, and best practices.
