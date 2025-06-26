package io.github.sd155.bego.tracker.domain

internal sealed class LocationError {
    data object IllegalState : LocationError()
    data object PermissionsDeniedByUser : LocationError()
    data object SettingsDeniedByUser : LocationError()
}