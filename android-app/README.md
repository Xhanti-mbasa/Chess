# IsoScout Android app

IsoScout is a lightweight sample Android application that lets you select local ISO, movie, series, or anime files (such as `.iso`, `.mp4`, `.mkv`, etc.). The app calls the Google Gemini API to generate a short summary describing the selected file.

## Features

- Pick multiple files from device storage using the system document picker
- Shows a queue of files with loading, success, and error states
- Sends file names to Gemini to identify likely platform or media type and produces concise summaries
- Built with Jetpack Compose, Kotlin coroutines, and OkHttp

## Getting started

1. Open the `android-app` directory in Android Studio Hedgehog or newer.
2. Provide a Gemini API key in one of the following ways:
   - Add a line to `~/.gradle/gradle.properties` (or `android-app/gradle.properties`) containing `GEMINI_API_KEY=your_key_here`.
   - Or set the environment variable `GEMINI_API_KEY` before launching the Gradle sync/build.
3. Sync the project and run the `app` configuration on an Android 7.0 (API 24) or newer device/emulator.

The sample key from the prompt (`AIzaSyD7QwVUZ-TvGy_-vTuBdf594dZi_EvauHQ`) can be used for quick testing, but you should replace it with a secret stored securely before shipping.

## Architecture overview

- **UI**: Jetpack Compose with a single activity hosting a `Scaffold` that shows a selectable button and a `LazyColumn` of analysis results.
- **State management**: `MainViewModel` exposes a `StateFlow` of `MainUiState` objects. Selecting files appends placeholders and asynchronously updates each entry.
- **Networking**: `GeminiClient` uses OkHttp and Moshi to send a `generateContent` request to the Gemini API with a concise prompt derived from the file name.

## Notes

- The app currently only sends file names (not file contents) to Gemini for classification to avoid uploading large files.
- Error messages from the Gemini API are surfaced per file so you can see when requests fail or keys are missing.
- For production usage, move Gemini interaction to a secure backend service and never ship API keys in client binaries.
