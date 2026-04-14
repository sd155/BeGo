package io.github.sd155.bego.tracker.platform.internal

import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import io.github.sd155.bego.di.DiTreeHolder
import io.github.sd155.bego.tracker.R
import io.github.sd155.bego.tracker.app.trackerModuleName
import io.github.sd155.bego.tracker.domain.Tracker
import io.github.sd155.bego.tracker.ui.UiFormatter
import io.github.sd155.logs.api.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class AndroidForegroundService : Service() {
    private val _logger by lazy {
        (application as? DiTreeHolder)?.diTree?.instance<Logger>(tag = trackerModuleName)
            ?: error("Application must implement DiTreeProvider")
    }
    private val _tracker by lazy {
        (application as? DiTreeHolder)?.diTree?.instance<Tracker>()
            ?: error("Application must implement DiTreeProvider")
    }
    private val _formatter = UiFormatter()
    private val _scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _notificationManager by lazy { getSystemService(NotificationManager::class.java) }
    private val _contentIntent by lazy { createContentIntent() }

    companion object {
        private const val CHANNEL_ID = "BegoTrackerForegroundServiceChannel"
        private const val NOTIFICATION_ID = 1
        private const val ACTION_START = "io.github.sd155.bego.tracker.action.START_FOREGROUND"
        private const val ACTION_STOP = "io.github.sd155.bego.tracker.action.STOP_FOREGROUND"
        @Volatile
        internal var isRunning: Boolean = false
            private set

        fun startService(context: Context) {
            val intent = Intent(context, AndroidForegroundService::class.java).apply {
                action = ACTION_START
            }
            context.startForegroundService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, AndroidForegroundService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        createNotificationChannel()
        _tracker.state
            .onEach { state ->
                if (!shouldRunForegroundService()) {
                    stopForegroundService()
                    return@onEach
                }
                _notificationManager.notify(NOTIFICATION_ID, createNotification(state.distance, state.pace))
            }
            .launchIn(_scope)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Tracker Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Used for keeping the stopwatch running in the background"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopForegroundService()
            return START_NOT_STICKY
        }
        if (!shouldRunForegroundService()) {
            stopForegroundService()
            return START_NOT_STICKY
        }
        startForeground()
        return START_NOT_STICKY
    }

    private fun startForeground() {
        try {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                createNotification(),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                } else {
                    0
                }
            )
        }
        catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            )
                _logger.error(event = "Invalid app state to start foreground service!", e = e)
            else
                _logger.error(event = "Failed to start foreground service!", e = e)
        }
    }

    private fun createNotification(
        distanceMeters: Double = _tracker.state.value.distance,
        paceMsPerKm: Long = _tracker.state.value.pace,
    ): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.foreground_service_icon)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(
                getString(
                    R.string.notification_content,
                    _formatter.formatDistance(distanceMeters),
                    _formatter.formatPace(paceMsPerKm),
                )
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setContentIntent(_contentIntent)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    getString(
                        R.string.notification_content,
                        _formatter.formatDistance(distanceMeters),
                        _formatter.formatPace(paceMsPerKm),
                    )
                )
            )
            .build()
    }

    private fun createContentIntent(): PendingIntent? {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            ?.apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            ?: return null
        val flags =
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(this, 0, launchIntent, flags)
    }

    private fun stopForegroundService() {
        if (!isRunning) {
            stopSelf()
            return
        }
        stopForegroundAndNotification()
        stopSelf()
    }

    private fun stopForegroundAndNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        _notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun shouldRunForegroundService(): Boolean =
        _tracker.state.value.running &&
            !ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

    override fun onDestroy() {
        isRunning = false
        stopForegroundAndNotification()
        _scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
