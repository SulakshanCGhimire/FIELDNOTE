# FieldNote

Offline-first Android application for structured field reporting and evidence management, with planned real-time collaboration.

![Status](https://img.shields.io/badge/status-pre--alpha-orange) ![Platform](https://img.shields.io/badge/platform-Android%207.0%2B-3DDC84) ![Language](https://img.shields.io/badge/language-Kotlin-7F52FF)

## Overview

Field teams such as researchers, inspectors, and community organizations frequently work where connectivity is unreliable. FieldNote lets users capture structured reports with photographs and optional location data, persist them locally, and synchronize with a backend when connectivity returns, without data loss or duplicate records.

## Status

Pre-alpha. The project scaffold builds and runs; feature development is in progress. The sections below describe the target design, not current functionality.

## Goals

- Local-first data model: the on-device database is the source of truth for displayed data.
- Durable, idempotent synchronization that tolerates interrupted requests and process death.
- Shared reports with backend-enforced authorization and real-time updates.
- Explicit conflict handling for concurrent edits.
- Reproducible setup, automated tests, and documented architectural decisions.

## Planned scope

| Priority | Scope |
|----------|-------|
| P0 | Report CRUD and search, Room persistence, draft recovery, photo attachments, optional location, durable sync queue, visible sync status, retry handling, persistent backend |
| P1 | Authentication, shared reports or workspaces, real-time updates, conflict resolution, reconnection recovery |
| P2 | Filtering, map view, export, accessibility and adaptive layouts |

## Architecture (target)

- **Android:** Kotlin, Jetpack Compose, Material 3, ViewModel with unidirectional data flow, Room, WorkManager, coroutines and Flow
- **Backend:** FastAPI, PostgreSQL
- **Sync:** client-generated stable IDs, idempotent server operations, WorkManager-driven background synchronization

Design rationale is recorded in [`docs/decisions`](docs/decisions).

## Repository structure

```text
.
├── android/          Android application (Gradle project)
├── backend/          FastAPI service (planned)
├── docs/decisions/   Architecture decision records
├── book-notes/       Reference notes
└── PROGRESS.md       Development log
```

## Getting started

**Requirements:** Android Studio (current stable), Android SDK 37, a device or emulator running Android 7.0 (API 24) or later.

1. Clone the repository.
2. Open the `android/` directory in Android Studio.
3. Allow Gradle to sync.
4. Run the `app` configuration.

Backend setup instructions will be added alongside the backend.

## Architecture decisions

| ADR | Decision |
|-----|----------|
| [0001](docs/decisions/0001-min-sdk-24.md) | Minimum SDK 24 |
| [0002](docs/decisions/0002-compose-over-views.md) | Jetpack Compose over XML Views |

## Known limitations

Only the application scaffold is implemented. No automated tests exist yet.