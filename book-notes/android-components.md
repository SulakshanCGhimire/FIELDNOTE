# Android Components (Book Ch. 3)

Book: *Programming Android, 2nd Ed.*, Ch. 3.

## 1. Activity
- **Concept:** An Android-managed unit of user interaction and UI with its own lifecycle. The system can start it from outside the app via intents.
- **Modern status:** Still current. Modern apps often use one Activity hosting many Compose screens.
- **FIELDNOTE:** A main Activity hosts the Compose UI and multiple screens.

## 2. Service
- **Concept:** A component for operations without a UI.
- **Modern status:** Still supported, but for reliable deferred work WorkManager is usually more appropriate.
- **FIELDNOTE:** Sync will likely use WorkManager, not a hand-written Service.

## 3. BroadcastReceiver
- **Concept:** Responds to broadcasts or system/application events.
- **Modern status:** Supported, but many implicit broadcasts are restricted in newer Android versions. Choose by requirement.
- **FIELDNOTE:** "Sync when the network returns" should use WorkManager network constraints, not a receiver.

## 4. ContentProvider
- **Concept:** Controlled access to structured data, especially when sharing between apps.
- **Modern status:** Still current for sharing data.
- **FIELDNOTE:** Not needed for the Room database. Sharing or exporting photos (Day 11) will likely use **FileProvider**, a ContentProvider subclass.

## Related notes
- `exported="true"` + an `<intent-filter>` with `MAIN` and `LAUNCHER` makes an Activity the app's launch entry point.
- Explicit intent names a component; implicit intent declares an action and the system resolves it via intent filters.

## Still to validate
- _Fill in after Day 2 when we implement navigation._