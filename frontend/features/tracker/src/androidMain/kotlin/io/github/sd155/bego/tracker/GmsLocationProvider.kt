package io.github.sd155.bego.tracker

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.AndroidLocationPrerequisites.Companion.LOCATION_INTERVAL_MS
import io.github.sd155.bego.utils.Result
import io.github.sd155.bego.utils.asFailure
import io.github.sd155.bego.utils.asSuccess
import io.github.sd155.bego.tracker.app.trackerModuleName
import io.github.sd155.bego.tracker.domain.LocationError
import io.github.sd155.bego.tracker.app.LocationProvider
import io.github.sd155.bego.tracker.domain.PlatformReason
import io.github.sd155.bego.tracker.domain.TrackPoint
import io.github.sd155.logs.api.Logger
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/**
 * Android implementation of LocationProvider using Google Play Services (Fused Location Provider).
 * Delivers TrackPoint updates after prerequisites are already satisfied.
 */
@OptIn(ExperimentalAtomicApi::class)
class GmsLocationProvider(
    applicationContext: Context,
) : LocationProvider() {
    private val _context = applicationContext
    private val _logger by lazy { Inject.instance<Logger>(tag = trackerModuleName) }
    private val _locationRequest by lazy {
        LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_INTERVAL_MS)
            .setMinUpdateIntervalMillis(LOCATION_INTERVAL_MS)
            .build()
    }
    private val _client by lazy { LocationServices.getFusedLocationProviderClient(_context) }
    private val _onLocation = AtomicReference<GmsLocationListener?>(null)

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
        _onLocation.exchange(newValue = null)?.let { listener ->
            _client.removeLocationUpdates(listener)
        }
    }

    override suspend fun sub(onUpdate: (TrackPoint) -> Unit): Result<LocationError, Unit> {
        return try {
            val listener = GmsLocationListener(onUpdate)
            _onLocation.store(listener)
            _client.requestLocationUpdates(_locationRequest, _context.mainExecutor, listener)
            Unit.asSuccess()
        }
        catch (e: SecurityException) {
            _logger.warn(event = "Location subscription denied due to missing permission", e = e)
            LocationError.PlatformFailure(reason = PlatformReason.Permissions).asFailure()
        }
    }
}
