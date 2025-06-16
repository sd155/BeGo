package io.github.sd155.bego

import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import io.github.sd155.bego.di.Inject
import io.github.sd155.logs.api.Logger

internal class BegoForegroundService : Service() {
    private val _logger by lazy { Inject.instance<Logger>(tag = PlatformDi.APPLICATION_MODULE_NAME) }

    companion object {
        private const val CHANNEL_ID = "BegoForegroundServiceChannel"
        private const val NOTIFICATION_ID = 1

        fun startService(context: Context) {
            val intent = Intent(context, BegoForegroundService::class.java)
            context.startForegroundService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, BegoForegroundService::class.java)
            context.stopService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Stopwatch Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Used for keeping the stopwatch running in the background"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun startForeground() {
        try {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                createNotification(),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
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

    private fun createNotification(): Notification {
        val platformConfig = Inject.instance<PlatformConfiguration>()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ver ${platformConfig.appVersion}")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
} 