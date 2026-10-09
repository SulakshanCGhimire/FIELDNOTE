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

## Day 3: Kotlin essentials and report model
 
**Objectives**
- Revise the Kotlin features needed for data modeling; design and implement the report domain model.

**Completed**
- Domain types in `domain/model`: `Priority`, `ReportStatus`, `Location` (self-validating), `Report` (immutable data class).
- Core library desugaring enabled so `java.time.Instant` works on minSdk 24.
- ADR 0004 (report domain model) written and accepted.
- `LocationTest` written and extended to five tests.

**Decisions**
- `categoryId` as a string, not an enum.
- `Instant` timestamps; `updatedAt` set in one place with an injectable clock (owning layer decided on Day 5).
- Sync state kept out of the domain model.
- Nullable `Location` in the model; persistence deferred.
- `DRAFT` is a synchronized domain status; title validated at submission, not construction.
- `version` and deletion marker deferred to the synchronization design.

**Concepts covered**
- Data classes, null safety, sealed types vs enums, coroutine basics, immutability with `copy()`, validation in `init`.

**Verification**
- `LocationTest`: 5 tests passed (valid coordinates, latitude at 90 accepted, latitude above 90 rejected, longitude above 180 rejected, negative accuracy rejected).
- Failure proof: with the longitude check temporarily commented out, `longitudeAbove180_isRejected` failed (4 passed, 1 failed). After restoring the check, 5 passed.
- Not tested: `Report` behavior, submission title validation, `updatedAt` handling, emulator.

**Needs revision**
- Where `updatedAt` is set (use case, repository or ViewModel).
- Display rule for untitled drafts in the report list.
- Draft visibility for collaborators (collaboration phase).

**Next**
- Day 4: Android View system versus Jetpack Compose and Material 3; first report list, detail and editor screens.
