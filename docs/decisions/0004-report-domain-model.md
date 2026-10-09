# ADR 0004: Report Domain Model

## Context
FieldNote needs a domain model for field reports that will be persisted locally (Room), synchronized with a backend, and later shared between collaborators. The model must support offline creation, evidence capture (including optional location), and future conflict handling, without committing early to details we cannot yet test.

The model is intentionally limited to the **domain type**. Database schema, API payloads, and synchronization metadata are separate concerns, designed in later milestones.

## Decisions

### 1. Category is an ID, not an enum
- **Options:** (a) fixed enum; (b) category ID (`String`) resolved against a list of categories.
- **Decision:** Store `categoryId: String`. Ship a small built-in default list now; add server-defined categories in the collaboration phase.
- **Reasoning:** FieldNote serves different kinds of teams, so a fixed enum would require an app release for every new category. A pure server-driven list is premature: there is no backend yet, and it raises unsolved questions (removed categories, first launch offline). An ID keeps the model ready for either.

### 2. Timestamps use `Instant`
- **Options:** (a) `Long` epoch milliseconds; (b) `java.time.Instant`.
- **Decision:** `createdAt` and `updatedAt` as `Instant`. An `observedAt` field may be added if a requirement appears.
- **Reasoning:** `Instant` is an unambiguous absolute point in time and is converted to local time only for display, which suits teams in different time zones.
- **Consequence:** `java.time` is natively available only from API 26, so with minSdk 24 (ADR 0001) the project enables core library desugaring (`isCoreLibraryDesugaringEnabled` plus the `coreLibraryDesugaring` dependency).
- **Limitation:** device clocks can be wrong, so `updatedAt` is informational and must not decide conflicts.

### 3. Sync state is local-only
- **Options:** (a) `syncState` on the domain `Report`; (b) sync metadata in a local storage type.
- **Decision:** Keep sync state out of `Report`.
- **Reasoning:** Domain state describes what a report *is*; sync state describes what *this device* must still do with the server. The server does not know about it.
- **Consequence:** The repository must combine report data with local sync metadata when the UI shows status such as "Pending". Separate API, database and domain types will be introduced only when needed.

### 4. Location is optional and in the model now
- **Decision:** `Report` has `location: Location?`. `Location` holds `latitude` and `longitude` (`Double`) and an optional accuracy in meters (`Double?`).
- **Persistence deferred:** database columns are decided during the persistence work.
- **Why "no location" is not further split:** whether the user declined permission, location was unavailable, or no location was wanted is handled as UI state when capture is implemented. The saved report only records whether coordinates exist. This avoids states that nothing downstream uses; revisit if a requirement needs the reason.
- **Privacy:** precise coordinates can reveal sensitive places, and synchronized coordinates are visible to every collaborator. Location stays optional and is requested only when needed. Precision can be reduced at capture time (for example, coarse location), which is easier than removing precision later.

### 5. Deferred fields
- `version` (optimistic concurrency) and a deletion marker are not added yet. Their semantics depend on the synchronization protocol, designed in Week 3 and the collaboration phase.

## Resulting model (conceptual)
`Report`: `id` (client-generated UUID string), `title`, `categoryId`, `description?`, `priority` (enum), `status` (enum), `createdAt`, `updatedAt`, `location?`.

## Open question
Does `ReportStatus.DRAFT` belong in the shared status enum? A saved, incomplete report is a business state, but if drafts are never sent to the server, `DRAFT` may be closer to local-only state. To be decided on Day 8 with draft recovery. Current view: _(add yours)_

## Consequences
- The model stays small and testable now, and the deferred items are recorded rather than forgotten.
- The repository layer carries the responsibility of joining domain data with local sync metadata.
- Desugaring adds one build dependency, justified by ADR 0001.