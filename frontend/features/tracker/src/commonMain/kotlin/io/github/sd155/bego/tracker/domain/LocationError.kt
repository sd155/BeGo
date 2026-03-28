package io.github.sd155.bego.tracker.domain

internal sealed class LocationError {
    data object IllegalState : LocationError()
    data class PlatformFailure(
        val reason: PlatformReason,
    ) : LocationError()
}

internal enum class PlatformReason {
    Permissions,
    Settings,
}