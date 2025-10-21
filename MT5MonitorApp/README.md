# MT5 Monitor Android App

This project delivers a Kotlin/Jetpack Compose Android application that connects to MetaTrader 5 Expert Advisors through the MetaApi.cloud bridge. The app focuses on real-time trading telemetry, push alerts, and manual/automatic trade controls tailored for mobile monitoring.

## Features

- **MetaApi connectivity** using Retrofit with Moshi serialization.
- **Dashboard** with live RSI gauge, MA(5) label, auto-trading switch, trade buttons, and EA parameter editor.
- **Positions** screen providing swipe-friendly list, chart overlays for price/RSI/MA, and close controls.
- **Settings & Security** including biometric readiness check, notification history, and room-backed persistence stubs.
- **Foreground monitoring service** to keep signals in sync with automatic reconnection support.
- **Firebase Cloud Messaging** integration for push notifications with custom channel configuration.

## Project Structure

```
MT5MonitorApp/
├── app/                      # Android application module
│   ├── build.gradle.kts      # Compose, Retrofit, Room, Hilt, Firebase dependencies
│   ├── src/main
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/mt5monitor
│   │   │   ├── data          # API, repository, and local database layers
│   │   │   ├── domain        # Core models for trading, chart, and notifications
│   │   │   ├── service       # Foreground monitor and FCM messaging services
│   │   │   ├── ui            # Compose UI, navigation, and view-model glue
│   │   │   └── ui/theme      # Dark trading theme
│   │   └── res               # Material theming resources and icons
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Getting Started

1. **Open in Android Studio** (Giraffe or newer recommended).
2. Allow Gradle to download dependencies and sync the project.
3. Create a `google-services.json` in `app/` if you wish to enable Firebase Cloud Messaging.
4. Supply MetaApi credentials (login, password, server, token) via your secure mechanism (e.g., encrypted `DataStore`). The repository currently uses a placeholder account id and should be extended before production.
5. Run on a device/emulator (API 26+). Foreground service permissions and notification runtime consent (Android 13+) may be required.

## Background Monitoring

`TradingMonitorService` polls MetaApi endpoints in a wake-lock friendly foreground service. Use `ContextCompat.startForegroundService` from app startup or WorkManager to keep monitoring active.

## Security Notes

- Integrate Android Keystore-backed `EncryptedSharedPreferences` or `EncryptedDataStore` for MT5 credentials before shipping.
- Enable biometric gating by wiring the `SettingsScreen` callback to your authentication flow.
- Consider obfuscating secrets with Proguard and R8 for release builds.

## Building an APK

```bash
./gradlew assembleDebug
```

The generated APK resides at `app/build/outputs/apk/debug/app-debug.apk` after a successful build.

## Testing

Unit and UI testing hooks are defined via JUnit4 and Compose UI Test dependencies. Extend the repository with fakes/mocks to validate trading logic before integrating live accounts.

## Next Steps

- Wire up actual MetaApi credentials and WebSocket streaming for lower latency.
- Persist notifications and EA settings using Room/Datastore with encryption.
- Implement manual authentication and session management for multiple accounts.
- Add MPAndroidChart-based advanced charting if Compose Canvas is insufficient.
