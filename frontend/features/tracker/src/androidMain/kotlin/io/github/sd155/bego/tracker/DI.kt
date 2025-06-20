package io.github.sd155.bego.tracker

import androidx.activity.ComponentActivity
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.domain.LocationProvider

class AndroidTrackerModuleDi {

    fun onCreateActivity(activity: ComponentActivity) =
        locationProvider().setActivity(activity)

    fun onResumeActivity(activity: ComponentActivity) =
        locationProvider().onResumeForeground(activity)

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