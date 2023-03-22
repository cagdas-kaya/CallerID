package com.callerid.Bcallerid.Utils

import android.Manifest
import com.callerid.Bcallerid.BuildConfig


class Constants {
    companion object {
        const val FOREGROUND_SERVICE_CHANNEL_ID = BuildConfig.APPLICATION_ID +"OverlayBubbleChannel"
        const val FOREGROUND_SERVICE_CHANNEL_NAME = BuildConfig.APPLICATION_ID +"OverlayBubble"
        const val SERVICE_INTENT_REQUEST_CODE = 385
        const val SERVICE_ID = 122
        const val PERMISSION_REQUEST_CODE = 245
        const val EXTRA_PHONE_NUMBER = "PhoneNumber"
        const val EXTRA_URL = "Url"
        const val INTENT_PHONE_STATE = "android.intent.action.PHONE_STATE"
        const val TEL_PREFIX = "tel:"
        const val MAPS_PREFIX = "maps"
        const val WHATSAPP_PREFIX = "https://wa.me/"
        const val PRINT_PREFIX = "print"
        const val URL_HOME = "https://www.example.com/"
        const val URL_CALLER_ID = "https://www.example.com/callerid/"
        const val TYPE_IMAGE = "image/*"
        val permissions = arrayOf<String>(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    }
}