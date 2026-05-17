package io.github.sd155.bego.tracker.platform.internal

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

internal class AndroidRuntime(
    context: Context,
    logger: Logger,
    tracker: Tracker,
) : DefaultLifecycleObserver {
    private val _logger: Logger by lazy { logger }
    private val _appContext by lazy { context.applicationContext }
    private val _scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _processLifecycle = ProcessLifecycleOwner.get().lifecycle
    private var _isAppInForeground = _processLifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    private var _isTracking = tracker.state.value.running

    init {
        _processLifecycle.addObserver(this)
        tracker.state
            .onEach { state ->
                _isTracking = state.running
                updateForegroundService()
            }
            .launchIn(_scope)
    }

    override fun onStart(owner: LifecycleOwner) {
        _isAppInForeground = true
        AndroidForegroundService.cancelCompletedNotification(_appContext)
        updateForegroundService()
    }

    override fun onStop(owner: LifecycleOwner) {
        _isAppInForeground = false
        updateForegroundService()
    }

    private fun updateForegroundService() {
        val shouldRunForegroundService = _isTracking && !_isAppInForeground
        if (shouldRunForegroundService) {
            if (AndroidForegroundService.isRunning) {
                return
            }
            _logger.debug(event = "Tracker moved to background, starting foreground service")
            AndroidForegroundService.startService(_appContext)
        }
        else {
            if (!AndroidForegroundService.isRunning) {
                return
            }
            _logger.debug(event = "Tracker is foreground-bound or idle, stopping foreground service")
            AndroidForegroundService.stopService(_appContext)
        }
    }
}
