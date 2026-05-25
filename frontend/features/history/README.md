# History Module

History storage module for tracker-emitted session points.
Back to the [BeGo frontend application](../../README.md)

## Features
- **Android persistence**: Android implementation backed by app-private append-only JSONL storage.
- **Session-oriented layout**: Stores each run session in its own file using `sessionStartTimeMs` as the file key.
- **Tracker-facing contract**: Exports `SessionPointConsumer` so app wiring can connect `tracker` to `history` without exposing storage internals.
- **Non-blocking writes**: Session points are accepted synchronously and persisted asynchronously on a dedicated sequential background scope.

## API
- `HistoryAndroidComponentsBuilder`
- `SessionPointConsumer`
- `historyModule((String) -> Logger, (Logger) -> SessionRepository): DiModule`

## Storage format
- Session files are stored under `files/history/sessions`.
- Each session uses one file named `<sessionStartTimeMs>.jsonl`.
- Each line is one JSON object representing a single `RunSessionPoint`.

## Structure
- `app`: DI contract, public `SessionPointConsumer`, and repository abstraction used by app wiring.
- `platform`: Android repository implementation and Android wiring helpers.
