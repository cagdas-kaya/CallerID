package com.callerid.Bcallerid.Services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.callerid.Bcallerid.MainActivity
import com.callerid.Bcallerid.OverlayFloatingButton
import com.callerid.Bcallerid.R
import com.callerid.Bcallerid.Utils.Constants


class OverlayFloatingButtonService : Service() {

    private lateinit var overlayFloatingButton: OverlayFloatingButton
    private var phoneNumber = ""
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        phoneNumber = intent!!.getStringExtra(Constants.EXTRA_PHONE_NUMBER).toString()
        createNotificationChannel()
        createService()
        overlayFloatingButton = OverlayFloatingButton(this)
        overlayFloatingButton.show(phoneNumber)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createService() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            Constants.SERVICE_INTENT_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_MUTABLE
        )
        val notification = NotificationCompat.Builder(this, Constants.FOREGROUND_SERVICE_CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text) + phoneNumber)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(Constants.SERVICE_ID, notification)

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                Constants.FOREGROUND_SERVICE_CHANNEL_ID,
                Constants.FOREGROUND_SERVICE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
}