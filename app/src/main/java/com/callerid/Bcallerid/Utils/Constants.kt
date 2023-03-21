package com.callerid.Bcallerid.Utils

import com.callerid.Bcallerid.BuildConfig


class Constants {
    companion object {
        const val FOREGROUND_SERVICE_CHANNEL_ID = BuildConfig.APPLICATION_ID +"OverlayBubbleChannel"
        const val FOREGROUND_SERVICE_CHANNEL_NAME = BuildConfig.APPLICATION_ID +"OverlayBubble"
        const val SERVICE_INTENT_REQUEST_CODE = 385
        const val SERVICE_ID = 122
        const val PERMISSION_REQUEST_CODE = 245
        const val EXTRA_PHONE_NUMBER = "PhoneNumber"
        const val INTENT_PHONE_STATE = "android.intent.action.PHONE_STATE"
    }
}