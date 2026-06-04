# SmartClipboardAI Jetpack Compose

This folder contains a native Android Jetpack Compose port of the React/Vite UI.

## What was converted

- Home screen with entry state, expanded collection dashboard, and AI recommendation card
- Data screen with photo permission states, partial photo selection, item filtering, multi-select, deletion, bottom sheet flow, and preview overlay
- Tasks screen with topic list and workflow indicator
- Topic detail screen with related data thumbnails, AI summary, source chips, and action draft cards
- Action review screen with editable drafts, target app metadata, version restore, AI chat preview/fullscreen chat, quick suggestions, and executed state
- Bottom navigation and modal analysis sheet

## Open in Android Studio

1. Open the `android-compose` folder as an Android project.
2. Let Android Studio sync Gradle.
3. Run the `app` configuration on an emulator or device.

The original React files were left in place so the Compose implementation can be compared against the source design.
