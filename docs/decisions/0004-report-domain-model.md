# ADR 0004: Report Domain Model

## Context

FieldNote needs a domain model for field reports that will be persisted locally using Room, synchronized with a backend, and eventually shared between collaborators. The model must support offline creation, evidence capture, optional location, and future conflict handling without committing prematurely to implementation details.

This decision covers the domain model. Database schemas, API payloads, and local synchronization metadata remain separate concerns.

## Decisions

### 1. Category is an ID, not an enum

**Options:** A fixed enum, or a category ID represented by a `String`.

**Decision:** Store `categoryId: String`. Provide a small built-in category list initially and introduce server-defined categories during the collaboration phase.

**Reasoning:** FieldNote serves different kinds of teams, so adding a category should not require an application release. A built-in list also supports first launch and offline use before the backend exists.

### 2. Timestamps use `Instant`

**Options:** `Long` epoch milliseconds, or `java.time.Instant`.

**Decision:** Use `Instant` for `createdAt` and `updatedAt`. Add `observedAt` only if a requirement emerges.

**Reasoning:** `Instant` represents an absolute point in time independently of the user's time zone. Local time conversion belongs in the presentation layer.

**Android compatibility:** `java.time` is natively available from API 26, but FieldNote supports API 24 (ADR 0001). Core library desugaring is therefore enabled using `isCoreLibraryDesugaringEnabled` and a `coreLibraryDesugaring` dependency.

Official reference: https://developer.android.com/studio/write/java8-support

**Limitation:** Device clocks can be incorrect or manually changed. `updatedAt` records and displays modification time but is not sufficient for reliable conflict detection.

### 3. Sync state is local-only

**Options:** Include `syncState` in the domain `Report`, or store it separately in local persistence metadata.

**Decision:** Keep `syncState` out of `Report`.

**Reasoning:** Domain state describes what a report is; sync state describes what a particular device needs to do with the server.

**Consequence:** The repository combines report data with local synchronization metadata when the UI needs both. Separate domain, database, and API representations are introduced only where necessary.

### 4. Location is optional in the domain model

**Options:** Add location to `Report` now, or defer the field until location capture is implemented (Day 10).

**Decision:** Include `location: Location?` in `Report` now. `Location` contains `latitude` and `longitude` as `Double` values and an optional `accuracyMeters: Double?`. Database columns and mappings are deferred to the persistence milestone.

**Reasoning:** Location is part of the report concept. Adding the type now avoids reshaping the model later, while deferring storage avoids premature schema decisions.

**Missing location:** A missing `Location` means no coordinates are attached. Whether permission was declined, location was unavailable, or the user chose not to attach coordinates is handled during location capture, unless a later requirement needs that reason persisted.

**Privacy:** Precise coordinates can reveal sensitive places. Location remains optional, is requested only when needed, and is shared only with appropriate authorization. Coarser location can be supported later if required.

### 5. Location validates its own invariants

**Options:** Validate inside `Location`, or validate in the code that creates it.

**Decision:** Validate in the `Location` initializer:

* Latitude must be between -90 and 90.
* Longitude must be between -180 and 180.
* Accuracy must not be negative when supplied.

**Reasoning:** The type should not allow invalid coordinates to exist as a valid `Location`. Validating only at creation sites would rely on every caller remembering to do so.

**Cost:** Invalid arguments throw `IllegalArgumentException`. Callers processing external input must validate first or handle the exception.

### 6. Version and deletion marker are deferred

**Options:** Add `version` and a deletion marker now, or defer them until the synchronization protocol is designed.

**Decision:** Defer both.

**Reasoning:** Their semantics depend on conflict detection, retries, and deletion propagation, which are designed in the synchronization and collaboration milestones. Adding them now would commit to semantics we cannot yet test.

**Follow-up:** Define versioning and deletion/tombstone handling before implementing reliable synchronization.

## Resulting model

The conceptual `Report` contains:

* `id`: client-generated UUID string
* `title`: non-null `String`
* `categoryId`: non-null `String`
* `description`: nullable `String`
* `priority`: `Priority` enum
* `status`: `ReportStatus` enum
* `createdAt`: `Instant`
* `updatedAt`: `Instant`
* `location`: nullable `Location`

The model uses immutable `val` properties. Defaults are provided for genuinely optional fields rather than for required identity or workflow information.

## Resolved questions

### 1. What does `ReportStatus.DRAFT` mean?

`ReportStatus.DRAFT` represents a saved report that is incomplete and has not yet been submitted. It is a domain status. Temporary editor text and Day 8 draft recovery remain separate local state.

**Consequences of treating DRAFT as a synchronized status:**

* The backend must accept incomplete reports and enforce title validation only on the transition to `SUBMITTED`.
* Collaborators may be able to see another user's drafts. Draft visibility is an open question for the collaboration phase.
* If the product later decides that drafts must stay on the device, storage and synchronization rules must be changed explicitly, and this ADR superseded.

### 2. Who updates `updatedAt` when a report changes?

`updatedAt` is set in exactly one place whenever a modification is accepted, using an injectable clock so behavior can be tested reliably. This avoids relying on every caller to remember the update, and keeps the data class free of timestamp side effects in `copy()`.

**Where that place lives** (a dedicated use case, the repository, or the ViewModel) is decided with the architecture discussion on Day 5, to keep the architecture proportional to the problem.

### 3. Should `Report` reject blank titles?

No. `Report` does not reject a blank title at construction, because an incomplete saved draft may legitimately have none.

A submission check validates that the title is not blank before a report may become `SUBMITTED`. Object construction stays flexible while the business rule is enforced at the workflow boundary.

**Consequence:** The report list needs a display rule for untitled drafts (decided with the Day 4 UI).

## Implementation and verification

The domain types are implemented in `domain/model`: `Priority`, `ReportStatus`, `Location`, and `Report`.

Core library desugaring is configured in Gradle and sync succeeded. `LocationTest` (valid coordinates, out-of-range latitude, negative accuracy) was run with the Gradle `testDebugUnitTest` task; the Kotlin sources compiled and the build was successful.

**Not yet tested:** `Report` behavior, submission-time title validation, and `updatedAt` handling. These need tests when their use cases are implemented. `LocationTest` also lacks boundary and longitude-rejection cases.

## Consequences

* The domain model remains small, immutable, and focused on business meaning.
* Categories can evolve without requiring an app release for every addition.
* Local synchronization mechanics stay out of the domain model.
* Location is supported in the domain before database details are committed.
* Core library desugaring adds build configuration and a dependency, justified by the API 24 minimum.
* Synchronized drafts require backend rules that differ by status, and a later decision on draft visibility.
* Title validation and timestamp updates have defined workflow responsibilities.
* Versioning and deletion handling remain explicit prerequisites for reliable synchronization.