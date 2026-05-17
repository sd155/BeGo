# Tracker API Module

Shared tracker contracts used across feature boundaries.
Back to the [BeGo frontend application](../../README.md)

## Features
- **Session point model**: Shared entity for tracker-emitted session samples.
- **Session identity by start time**: Each point carries `sessionStartTimeMs`, which identifies the run session for consumers.
- **Cross-feature contract**: Keeps `tracker` decoupled from downstream storage and history feature implementation details.

## API
- `RunSessionPoint`

## Structure
- `api`: Shared contracts used by `tracker`, app wiring, and downstream consumers such as history persistence.
