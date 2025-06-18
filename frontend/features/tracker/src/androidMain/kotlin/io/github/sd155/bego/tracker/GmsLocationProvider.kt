package io.github.sd155.bego.tracker

import android.Manifest
import android.content.IntentSender
import android.location.Location
import android.os.Looper
import androidx.activity.ComponentActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.result.Result
import io.github.sd155.bego.result.asFailure
import io.github.sd155.bego.result.asSuccess
import io.github.sd155.bego.result.next
import io.github.sd155.bego.tracker.di.trackerModuleName
import io.github.sd155.bego.tracker.domain.LocationError
import io.github.sd155.bego.tracker.domain.LocationProvider
import io.github.sd155.bego.tracker.domain.TrackPoint
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import java.lang.ref.WeakReference
import java.util.concurrent.Executor
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GmsLocationProvider : LocationProvider() {
    private val _locationIntervalMs = 1000L
    private val _logger by lazy { Inject.instance<Logger>(tag = trackerModuleName) }
    private var activityRef: WeakReference<ComponentActivity>? = null
    private val _permissions by lazy { AndroidPermissionValidator(_logger) }
    private var locationRequest: LocationRequest? = null
    private var onLocation: GmsLocationListener? = null

    fun setActivity(activity: ComponentActivity) {
        activityRef = WeakReference(activity)
        _permissions.setup(activity)
    }

    override suspend fun start(): Result<LocationError, Unit> {
        return withActivity { activity ->
            _permissions.checkAndRequest(
                activity = activity,
                permissions = arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                .let { isPermissionsGranted ->
                    if (isPermissionsGranted) Unit.asSuccess()
                    else LocationError.PermissionsDeniedByUser.asFailure()
                }
                .next { checkLocationSettings(activity) }
                .next { locationRequest ->
                    this.locationRequest = locationRequest
                    Unit.asSuccess()
                }
        }
    }

    private inline fun <S> withActivity(
        block: (ComponentActivity) -> Result<LocationError, S>
    ): Result<LocationError, S> {
        val activity = activityRef?.get()
        return if (activity == null)
            LocationError.IllegalState.asFailure()
        else
            block(activity)
    }

    private suspend fun checkLocationSettings(activity: ComponentActivity): Result<LocationError, LocationRequest> {
        val request = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, _locationIntervalMs)
            .setMinUpdateIntervalMillis(_locationIntervalMs)
            .build()
        return suspendCoroutine { continuation ->
            val settingsRequest = LocationSettingsRequest
                .Builder()
                .addLocationRequest(request)
                .build()
            LocationServices.getSettingsClient(activity)
                .checkLocationSettings(settingsRequest)
                .addOnFailureListener { e ->
                    if (e is ResolvableApiException) {
                        try {
                            _logger.info(event = "Location system settings are not relevant, trying to resolve..")
                            val requestCode = 123
                            e.startResolutionForResult(activity, requestCode)
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
            .next { request.asSuccess() }
    }

    private class GmsLocationListener(
        private val onUpdate: (TrackPoint) -> Unit
    ) : LocationListener {

        override fun onLocationChanged(location: Location) {
            onUpdate(
                TrackPoint(
                    timeMs = location.time,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    horizontalAccuracyMeters = location.accuracy,
                    altitudeMeters = location.altitude,
                    altitudeAccuracyMeters = location.verticalAccuracyMeters,
                    speedMetersPerSecond = location.speed,
                    speedAccuracyMeterPerSecond = location.speedAccuracyMetersPerSecond,
                )
            )
        }
    }

    override fun unsub() {
        withActivity { activity ->
            onLocation?.let { listener ->
                LocationServices.getFusedLocationProviderClient(activity)
                    .removeLocationUpdates(listener)
            }
            Unit.asSuccess()
        }
    }

    override suspend fun sub(onUpdate: (TrackPoint) -> Unit): Result<LocationError, Unit> {
        return try {
            withActivity  { activity ->
                val request = locationRequest
                    ?: return LocationError.NotStarted.asFailure()
                val listener = GmsLocationListener(onUpdate)
                onLocation = listener
                getExecutorOrNull()
                    ?.let { executor ->
                        LocationServices.getFusedLocationProviderClient(activity)
                            .requestLocationUpdates(request, executor, listener)
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
                            .requestLocationUpdates(request, listener, looper)
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