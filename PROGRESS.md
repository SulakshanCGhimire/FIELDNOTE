# Development Log

## Current status

| Item | State |
|------|-------|
| Phase | Foundations (Day 1, in progress) |
| Last verified checkpoint | Starter Compose app built and run on a physical device (Android 15) |
| Open blockers | None |
| Next milestone | Component model and navigation skeleton |

## Day 1: Environment and project scaffold

**Objectives**
- Set up the toolchain, create the Compose project, establish the repository, record initial decisions.

**Completed**
- Android Studio installed; project created at `android/` (package `np.com.sulakshan.fieldnote`, minSdk 24, compile/target SDK 37).
- Starter app run on a physical device.
- ADR 0001 (minimum SDK) and ADR 0002 (Compose over Views) written.
- README, development log, and component notes created.
- Git repository initialized.

**Decisions**
- Android Studio for the Android module; VS Code for the backend.
- minSdk 24 (about 99.2% device coverage per the project wizard).
- Jetpack Compose as the primary UI toolkit.

**Concepts covered**
- Application components, intents (explicit and implicit), manifest launcher entry, configuration change versus process death, minSdk/compileSdk/targetSdk.

**Needs revision**
- Precise definition of an Activity; the roles of `exported` versus intent filters.

**Verification**
- Manual: app launches and renders on device.
- Automated tests: none run. Emulator: not tested.

**Open items**
- Verify and record the first commit.
- Complete end-of-day review.

## Day 2: Components and navigation 

**Objectives**
- Understand the Activity lifecycle, design navigation, and implement a placeholder navigation flow.

**Completed**
- Added Navigation Compose via the version catalog.
- Implemented routes `reports`, `report/{id}`, `editor?id={id}` with placeholder screens in `ui/navigation`.
- `MainActivity` hosts `FieldNoteNavHost`.
- ADR 0003 (single-activity navigation) written.

**Decisions**
- Single Activity with navigation graph; ID-only arguments; one editor route with an optional ID.
- Back with unsaved changes: confirmation dialog now, auto-saved drafts later (Day 8).

**Concepts covered**
- Lifecycle callbacks, Home vs Back, configuration change vs process death, back stack, version catalog vs compatibility.

**Verification**
- Manual on device: build and launch OK; back-stack behavior matched predictions.
- Untested: rotation on the editor screen, emulator. Automated tests: none.

**Needs revision**
- Which lifecycle callbacks are guaranteed (`onPause`/`onStop`); catalog centralizes versions but does not guarantee compatibility.

**Next**
- Day 3: Kotlin essentials, report data model and UI state.