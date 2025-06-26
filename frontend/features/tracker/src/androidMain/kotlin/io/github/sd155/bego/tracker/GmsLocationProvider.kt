package io.github.sd155.bego.tracker

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.activity.ComponentActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.utils.Result
import io.github.sd155.bego.utils.SafeContinuation
import io.github.sd155.bego.utils.asFailure
import io.github.sd155.bego.utils.asSuccess
import io.github.sd155.bego.utils.next
import io.github.sd155.bego.tracker.di.trackerModuleName
import io.github.sd155.bego.tracker.domain.LocationError
import io.github.sd155.bego.tracker.domain.LocationProvider
import io.github.sd155.bego.tracker.domain.TrackPoint
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import java.lang.ref.WeakReference
import java.util.concurrent.Executor
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Android implementation of LocationProvider using Google Play Services (Fused Location Provider).
 * Handles permission checks, location settings, and delivers TrackPoint updates.
 */
@OptIn(ExperimentalAtomicApi::class)
class GmsLocationProvider : LocationProvider() {
    private val _locationIntervalMs = 1000L
    private val _logger by lazy { Inject.instance<Logger>(tag = trackerModuleName) }
    private val _permissions by lazy { AndroidPermissionValidator(_logger) }
    private val _activityRef = AtomicReference<WeakReference<ComponentActivity>?>(null)
    private val _locationRequest by lazy {
        LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, _locationIntervalMs)
            .setMinUpdateIntervalMillis(_locationIntervalMs)
            .build()
    }
    private val _onLocation = AtomicReference<GmsLocationListener?>(null)
    private val _settingsContinuation by lazy {
        SafeContinuation<Result<LocationError, Unit>>(
            onAlreadyResumed = { _logger.warn(event = "System location settings continuation is already resumed!") },
            onNotResumed = { continuation ->
                _logger.warn(event = "System location settings continuation is not resumed yet! Resuming it with error to release..")
                continuation.resume(LocationError.IllegalState.asFailure())
            }
        )
    }
    private val _settingsRequestCode = 123

    internal fun setActivity(activity: ComponentActivity) {
        _activityRef.store(WeakReference(activity))
        _permissions.setup(activity)
    }

    internal fun onLostForeground(context: Context) {
        _onLocation.load()?.let {
            TrackerForegroundService.startService(context)
        }
    }

    internal fun onResumeForeground(context: Context) =
        TrackerForegroundService.stopService(context)

    internal fun onActivityDestroyed() {
        _settingsContinuation.close()
    }

    internal fun onActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == _settingsRequestCode) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                _logger.info(event = "Location settings resolution successful")
                _settingsContinuation.resume(Unit.asSuccess())
            } else {
                _logger.info(event = "Location settings resolution cancelled by user")
                _settingsContinuation.resume(LocationError.SettingsDeniedByUser.asFailure())
            }
        }
    }

    override suspend fun start(): Result<LocationError, Unit> =
        withActivity { activity ->
            _permissions.checkAndRequest(
                activity = activity,
                permissions = locationPermissions()
            )
                .let { isPermissionsGranted ->
                    if (isPermissionsGranted) Unit.asSuccess()
                    else LocationError.PermissionsDeniedByUser.asFailure()
                }
                .next { checkLocationSettings(activity) }
        }

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

    private inline fun <S> withActivity(
        block: (ComponentActivity) -> Result<LocationError, S>
    ): Result<LocationError, S> {
        val activity = _activityRef.load()?.get()
        return if (activity == null)
            LocationError.IllegalState.asFailure()
        else
            block(activity)
    }

    private suspend fun checkLocationSettings(activity: ComponentActivity): Result<LocationError, Unit> {
        return suspendCoroutine { continuation ->
            val settingsRequest = LocationSettingsRequest
                .Builder()
                .addLocationRequest(_locationRequest)
                .build()
            LocationServices.getSettingsClient(activity)
                .checkLocationSettings(settingsRequest)
                .addOnFailureListener { e ->
                    if (e is ResolvableApiException) {
                        try {
                            _logger.info(event = "Location system settings are not relevant, trying to resolve..")
                            _settingsContinuation.store(continuation)
                            e.startResolutionForResult(activity, _settingsRequestCode)
                        }
                        catch (sendEx: IntentSender.SendIntentException) {
                            _logger.warn(event = "Failed to start location settings resolution!", e = sendEx)
                            continuation.resume(LocationError.SettingsDeniedByUser.asFailure())
                        }
                    }
                    else {
                        _logger.warn(event = "Location system settings are not relevant, and cannot be resolved", e = e)
                        continuation.resume(LocationError.SettingsDeniedByUser.asFailure())
                    }
                }
                .addOnSuccessListener {
                    continuation.resume(Unit.asSuccess())
                }
        }
    }

    private class GmsLocationListener(
        private val onUpdate: (TrackPoint) -> Unit
    ) : LocationListener {

        override fun onLocationChanged(location: Location) {
            onUpdate(
                TrackPoint(
                    timeMs = location.time,
                    latitudeDegrees = location.latitude,
                    longitudeDegrees = location.longitude,
                    horizontalAccuracyMeters = location.accuracy,//opt
                    altitudeMeters = location.altitude,//opt
                    altitudeAccuracyMeters = location.verticalAccuracyMeters,//opt
                    speedMetersPerSecond = location.speed,//opt
                    speedAccuracyMeterPerSecond = location.speedAccuracyMetersPerSecond,//opt
                    bearingDegrees = location.bearing,//opt
                    bearingAccuracyDegrees = location.bearingAccuracyDegrees//opt
                )
            )
        }
    }

    override fun unsub() {
        withActivity { activity ->
            _onLocation.exchange(newValue = null)?.let { listener ->
                LocationServices.getFusedLocationProviderClient(activity)
                    .removeLocationUpdates(listener)
            }
            Unit.asSuccess()
        }
    }

    override suspend fun sub(onUpdate: (TrackPoint) -> Unit): Result<LocationError, Unit> {
        return try {
            withActivity  { activity ->
                val listener = GmsLocationListener(onUpdate)
                _onLocation.store(listener)
                getExecutorOrNull()
                    ?.let { executor ->
                        LocationServices.getFusedLocationProviderClient(activity)
                            .requestLocationUpdates(_locationRequest, executor, listener)
                        Unit.asSuccess()
                    }
                    ?: let {
                        _logger.warn(event = "LocationProvider seems to be running on main thread!")
                        val looper = Looper.myLooper()
                            ?: run {
                                _logger.error(event = "Failed to subscribe on location updates due to no Looper or Executor found!")
                                return LocationError.IllegalState.asFailure()
                            }
                        LocationServices.getFusedLocationProviderClient(activity)
                            .requestLocationUpdates(_locationRequest, listener, looper)
                        Unit.asSuccess()
                    }
            }
        }
        catch (e: SecurityException) {
            LocationError.PermissionsDeniedByUser.asFailure()
        }
    }

    private suspend fun getExecutorOrNull(): Executor? =
        coroutineContext[ContinuationInterceptor]
            .let { dispatcher ->
                if (dispatcher is ExecutorCoroutineDispatcher)
                    dispatcher.executor
                else
                    null
            }
}