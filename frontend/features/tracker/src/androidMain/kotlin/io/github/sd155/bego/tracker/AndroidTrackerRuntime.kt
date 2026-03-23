package io.github.sd155.bego.tracker

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import io.github.sd155.bego.tracker.domain.Tracker
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class AndroidTrackerRuntime(
    context: Context,
    tracker: Tracker,
    private val logger: Logger,
) : DefaultLifecycleObserver {
    private val appContext = context.applicationContext
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val processLifecycle = ProcessLifecycleOwner.get().lifecycle
    private var isAppInForeground = processLifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    private var isTracking = tracker.state.value.running

    init {
        processLifecycle.addObserver(this)
        tracker.state
            .onEach { state ->
                isTracking = state.running
                updateForegroundService()
            }
            .launchIn(scope)
    }

    override fun onStart(owner: LifecycleOwner) {
        isAppInForeground = true
        updateForegroundService()
    }

    override fun onStop(owner: LifecycleOwner) {
        isAppInForeground = false
        updateForegroundService()
    }

    private fun updateForegroundService() {
        if (isTracking && !isAppInForeground) {
            logger.debug(event = "Tracker moved to background, starting foreground service")
            TrackerForegroundService.startService(appContext)
        } else {
            logger.debug(event = "Tracker is foreground-bound or idle, stopping foreground service")
            TrackerForegroundService.stopService(appContext)
        }
    }
}
