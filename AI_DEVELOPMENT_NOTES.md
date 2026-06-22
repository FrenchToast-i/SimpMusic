# AI Development Notes for SimpMusic/VibeMusic

## Overview
This document provides essential information for AI agents working on the SimpMusic/VibeMusic Kotlin Multiplatform music player application. It covers key learnings, build processes, and common issues encountered during development.

## Project Structure
- **Kotlin Multiplatform**: Targets Android, JVM/Desktop, and iOS (future)
- **Architecture**: Clean Architecture with separation of data, domain, and presentation layers
- **Core Modules**: Located in `core/` directory (common, data, domain, service modules)
- **Service Modules**: ktorExt, aiService, lyricsService, kotlinYtmusicScraper, spotify, kizzy
- **Media Modules**: media-jvm, media-jvm-ui, media3, media3-ui
- **UI Modules**: composeApp (common UI), androidApp (Android-specific), desktopApp (Desktop-specific)

## Build Requirements

### Linux Build Setup
```bash
# Set Android SDK environment variables
export ANDROID_HOME=$HOME/Android/Sdk
export ANDROID_SDK_ROOT=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools

# Create local.properties file
echo "sdk.dir=$ANDROID_HOME" > local.properties

# Initialize git submodules (if using VibeMusic fork)
git submodule update --init --recursive

# Build debug APK
./gradlew assembleDebug

# Install via ADB
adb install -r androidApp/build/outputs/apk/debug/androidApp-arm64-v8a-debug.apk
```

### Common Build Issues

#### SDK Location Not Found
**Problem**: Gradle fails with "SDK location not found" error even when local.properties exists.

**Solution**: Set both ANDROID_HOME and ANDROID_SDK_ROOT environment variables:
```bash
export ANDROID_HOME=/path/to/Android/Sdk
export ANDROID_SDK_ROOT=/path/to/Android/Sdk
```

#### Configuration Cache Issues
**Problem**: Build fails with configuration cache errors.

**Solution**: Disable configuration cache:
```bash
./gradlew assembleDebug --no-configuration-cache
```

#### Gradle Daemon Issues
**Problem**: Build hangs or behaves unexpectedly.

**Solution**: Stop the gradle daemon:
```bash
./gradlew --stop
```

## Last.fm Integration

### Implementation Details
The Last.fm integration is fully implemented in `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/service/lastfm/LastFmService.kt`.

**Key Features**:
- OAuth authentication flow
- Scrobbling support
- Now playing updates
- Top artists/tracks retrieval
- Similar tracks recommendations
- API signature generation with MD5 hashing

**Required Imports**:
```kotlin
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
```

**API Configuration**:
- BASE_URL: `https://ws.audioscrobbler.com/2.0/`
- AUTH_URL: `https://www.last.fm/api/auth/`
- Json configuration: `ignoreUnknownKeys = true`

### Common JSON Parsing Issues
When working with Last.fm API responses, ensure you have the correct imports for JSON parsing. The `jsonArray` extension function must be imported from `kotlinx.serialization.json.jsonArray`.

## Feature Removals Completed
The following features have been removed from the application as requested:
- Proxy settings (from SettingScreen.kt)
- About Us section (from SettingScreen.kt)
- Backup Discord integration (from SettingScreen.kt)
- SimpMusic contributions/contributor settings (from SettingScreen.kt)
- Unnecessary AI options (from SettingScreen.kt)

## UI Components

### Local Playlist Screen
Located in `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/library/LocalPlaylistScreen.kt`

**Key Features**:
- Drag-and-drop track reordering
- Search functionality
- AI recommendations
- YouTube sync support
- Apple Music-style immersive header for mobile portrait

### YouTube Playlist Screen
Located in `composeApp/src/commonMain/kotlin/com/maxrave/simpmusic/ui/screen/other/PlaylistScreen.kt`

**Note**: This screen should be visually unified with LocalPlaylistScreen but currently uses a different UI structure.

## Data Persistence
- **Preferences**: Jetpack DataStore for storing user preferences
- **Database**: Room for local data storage
- **Offline Support**: Offline scrobble tracking for Last.fm

## Key Dependencies
- **Ktor**: HTTP client for API calls
- **Kotlinx.serialization**: JSON parsing
- **Jetpack Compose**: UI framework
- **Koin**: Dependency injection
- **Room**: Database
- **DataStore**: Preferences storage

## Development Workflow

### Making Changes
1. Edit the relevant source files
2. Test changes locally
3. Build APK: `./gradlew assembleDebug`
4. Install on device: `adb install -r androidApp/build/outputs/apk/debug/androidApp-arm64-v8a-debug.apk`
5. Commit changes with descriptive messages
6. Push to remote repository

### Git Workflow
- Main repository: `maxrave-dev/SimpMusic`
- Fork repository: `FrenchToast-i/VibeMusic`
- Remote name: `vibemusic`

### Testing
- Unit tests: `./gradlew test`
- Android tests: `./gradlew connectedAndroidTest`
- Integration tests should be run on actual devices/emulators

## Known Issues and Workarounds

### Icon Resource References
Some drawable references like `baseline_smart_shuffle_24` may not exist. Use Compose Icons instead:
```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Shuffle
Icons.Rounded.Shuffle
```

### Syntax Errors
Always ensure proper brace closure in Composable functions, especially after large edits.

### Gradle Properties
Never append to gradle.properties without proper newlines. Corrupted gradle.properties files can cause parsing errors. Restore from git if corrupted:
```bash
git checkout gradle.properties
```

## Performance Considerations
- Use `collectAsStateWithLifecycle` for state collection in Composables
- Implement proper pagination for large lists
- Use remember functions for expensive computations
- Optimize image loading with Coil

## Platform-Specific Notes

### Android
- Minimum SDK: Defined in androidApp/build.gradle.kts
- Target SDK: Defined in androidApp/build.gradle.kts
- Permissions: Defined in AndroidManifest.xml

### Desktop
- Uses Conveyor for packaging
- Supports Windows, macOS, and Linux

### iOS (Future)
- Not yet implemented
- Will require additional platform-specific setup

## Contact and Resources
- Original Repository: https://github.com/maxrave-dev/SimpMusic
- Fork Repository: https://github.com/FrenchToast-i/VibeMusic
- Documentation: Check README.md in project root

## Recent Changes Summary
- Removed unwanted features (proxy, about us, backup discord, contributions, AI options)
- Implemented full Last.fm integration with OAuth and scrobbling
- Fixed compilation errors in LastFmService.kt by adding missing imports
- Successfully built and deployed Android APK
- Pushed changes to FrenchToast-i/VibeMusic repository

## Future Work
- Unify YouTube playlist UI with local playlist UI
- Implement iOS platform support
- Add more comprehensive testing
- Optimize performance for large playlists
