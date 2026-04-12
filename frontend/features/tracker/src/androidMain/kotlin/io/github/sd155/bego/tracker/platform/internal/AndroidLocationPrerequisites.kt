package io.github.sd155.bego.tracker.platform.internal

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import io.github.sd155.bego.tracker.platform.utils.AndroidPermissionValidator
import io.github.sd155.bego.tracker.app.LocationPrerequisites
import io.github.sd155.bego.tracker.domain.LocationError
import io.github.sd155.bego.tracker.domain.PlatformReason
import io.github.sd155.bego.utils.Result
import io.github.sd155.bego.utils.SafeContinuation
import io.github.sd155.bego.utils.asFailure
import io.github.sd155.bego.utils.asSuccess
import io.github.sd155.bego.utils.next
import io.github.sd155.logs.api.Logger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class AndroidLocationPrerequisites(
    private val activity: ComponentActivity,
    private val logger: Logger,
    private val permissions: AndroidPermissionValidator,
    private val settingsLauncher: ActivityResultLauncher<IntentSenderRequest>,
) : LocationPrerequisites(), DefaultLifecycleObserver {
    private val locationRequest = LocationRequest
        .Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_INTERVAL_MS)
        .setMinUpdateIntervalMillis(LOCATION_INTERVAL_MS)
        .build()
    private val settingsContinuation = SafeContinuation<Result<LocationError, Unit>>(
        onAlreadyResumed = { logger.warn(event = "System location settings continuation is already resumed!") },
        onNotResumed = { continuation ->
            logger.warn(event = "System location settings continuation is not resumed yet! Resuming it with error to release..")
            continuation.resume(LocationError.IllegalState.asFailure())
        }
    )
    internal companion object {
        internal const val LOCATION_INTERVAL_MS = 1000L
    }

    init {
        activity.lifecycle.addObserver(this)
    }

    fun onSettingsResolutionResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            logger.info(event = "Location settings resolution successful")
            settingsContinuation.resume(Unit.asSuccess())
        } else {
            logger.info(event = "Location settings resolution cancelled by user")
            settingsContinuation.resume(LocationError.PlatformFailure(reason = PlatformReason.Settings).asFailure())
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        settingsContinuation.close()
        owner.lifecycle.removeObserver(this)
    }

    override suspend fun ensureReady(): Result<LocationError, Unit> =
        permissions.checkAndRequest(
            context = activity,
            permissions = locationPermissions()
        )
            .let { isPermissionsGranted ->
                if (isPermissionsGranted) Unit.asSuccess()
                else LocationError.PlatformFailure(reason = PlatformReason.Permissions).asFailure()
            }
            .next { checkLocationSettings() }

    private fun locationPermissions(): Array<String> =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        else
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )

    private suspend fun checkLocationSettings(): Result<LocationError, Unit> =
        suspendCoroutine { continuation ->
            val settingsRequest = LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest)
                .build()
            LocationServices.getSettingsClient(activity)
                .checkLocationSettings(settingsRequest)
                .addOnFailureListener { error ->
                    if (error is ResolvableApiException) {
                        try {
                            logger.info(event = "Location system settings are not relevant, trying to resolve..")
                            settingsContinuation.store(continuation)
                            settingsLauncher.launch(
                                IntentSenderRequest.Builder(error.resolution).build()
                            )
                        }
                        catch (sendEx: IntentSender.SendIntentException) {
                            logger.warn(event = "Failed to start location settings resolution!", e = sendEx)
                            continuation.resume(LocationError.PlatformFailure(reason = PlatformReason.Settings).asFailure())
                        }
                    }
                    else {
                        logger.warn(event = "Location system settings are not relevant, and cannot be resolved", e = error)
                        continuation.resume(LocationError.PlatformFailure(reason = PlatformReason.Settings).asFailure())
                    }
                }
                .addOnSuccessListener {
                    continuation.resume(Unit.asSuccess())
                }
        }
}