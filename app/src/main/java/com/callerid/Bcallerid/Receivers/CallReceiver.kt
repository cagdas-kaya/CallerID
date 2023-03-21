package com.callerid.Bcallerid.Receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.callerid.Bcallerid.Services.OverlayFloatingButtonService
import com.callerid.Bcallerid.Utils.Constants
import com.callerid.Bcallerid.Utils.Utils


class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Constants.INTENT_PHONE_STATE) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                val number = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
                if (number != null) {
                    if (!Utils.isMyServiceRunning(context!!, OverlayFloatingButtonService::class.java)) {
                        overlayPermissionControl(context, number)
                    }
                }
            }
        }
    }

    private fun startOverlayService(context: Context, number: String) {
        val serviceIntent = Intent(context, OverlayFloatingButtonService::class.java)
        serviceIntent.putExtra(Constants.EXTRA_PHONE_NUMBER, number)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    private fun overlayPermissionControl(context: Context, number: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                startOverlayService(context, number)
            }
        }
    }

}
