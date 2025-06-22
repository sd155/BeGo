package io.github.sd155.bego.tracker

import androidx.activity.ComponentActivity
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.domain.LocationProvider

/**
 * Android DI helper for wiring up the tracker module with the current activity.
 * Ensures the correct LocationProvider is used and lifecycle events are handled.
 */
class AndroidTrackerModuleDi {

    /**
     * Call from Activity.onCreate to set up the location provider.
     */
    fun onCreateActivity(activity: ComponentActivity) =
        locationProvider().setActivity(activity)

    /**
     * Call from Activity.onResume to handle foreground transition.
     */
    fun onResumeActivity(activity: ComponentActivity) =
        locationProvider().onResumeForeground(activity)

    /**
     * Call from Activity.onPause to handle background transition.
     */
    fun onPauseActivity(activity: ComponentActivity) =
        locationProvider().onLostForeground(activity)

    private fun locationProvider(): GmsLocationProvider =
        Inject.instance<LocationProvider>()
            .let { instance ->
                if (instance is GmsLocationProvider) instance
                else null
            }
            ?: throw IllegalStateException("No proper LocationProvider instance")

}