# ADR 0003: Single-Activity Navigation

## Context
FieldNote has multiple destinations: a report list, a report detail view, and a report editor used for both creating and editing reports. We need a navigation structure that keeps these destinations connected, makes back navigation predictable, and is testable.

## Options

### Option 1: One Activity per screen
- **Advantage:** Each screen has its own Activity and lifecycle, and the model matches the traditional approach described in the reference book.
- **Disadvantage:** Data between screens travels through Intent extras, shared state is harder to coordinate, and each Activity needs its own manifest entry. Navigation logic is spread across many classes and is harder to test.

### Option 2: One Activity with a navigation graph
- **Advantage:** A single Activity hosts all Compose destinations. A navigation controller owns the back stack, and routes are defined in one place.
- **Disadvantage:** The navigation graph becomes a central point of coupling, and the team must learn the navigation library.

## Decision
Use a single `MainActivity` hosting a navigation graph built with **Navigation Compose**.

Navigation 3 is also documented by the official guidance, but the standard Compose setup guide currently uses Navigation Compose, so we adopt it now and will revisit if the official recommendation changes. The exact dependency version is managed in `libs.versions.toml` and was taken from the official setup page.

### Routes
| Route | Purpose |
|-------|---------|
| `reports` | Report list |
| `report/{id}` | Report detail |
| `editor?id={id}` | Report editor; no ID creates a new report, an ID edits an existing one |

### Route design
- Detail and editor destinations receive only the report **ID**, not the whole object. The ID is small and easy to restore, and the local database remains the source of truth, so a screen always loads current data.
- The editor uses **one route with an optional ID** instead of separate `editor/new` and `editor/{id}` routes. The same editor UI serves both cases, route wiring is not duplicated, and no special `"new"` value can collide with a real ID.

## Consequences
- Navigation and back-stack behavior are managed centrally and can be tested without launching multiple Activities.
- The editor must determine whether it is creating or editing from the presence of an ID.
- Reports are identified by a client-generated UUID, which will later also serve as the idempotency key for synchronization.
- **Back with unsaved changes:** initially a confirmation dialog (save or discard). The longer-term target is auto-saved drafts so leaving the editor never loses work (planned for Day 8).