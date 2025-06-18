package io.github.sd155.bego.tracker

import androidx.activity.ComponentActivity
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.domain.LocationProvider

class AndroidTrackerModuleDi {

    fun onCreateActivity(activity: ComponentActivity) {
        Inject.instance<LocationProvider>()
            .let { instance ->
                if (instance is GmsLocationProvider) instance
                else null
            }?.setActivity(activity)
            ?: throw IllegalStateException("No proper LocationProvider instance")
    }
}