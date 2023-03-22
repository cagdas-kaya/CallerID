package com.callerid.Bcallerid.Utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.GeolocationPermissions
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity


class MyWebChromeClient(
    private val activity: AppCompatActivity,
) : WebChromeClient() {

    override fun onShowFileChooser(
        mWebView: WebView?,
        filePathCallback: ValueCallback<Array<Uri?>?>,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        mFilePathCallback = filePathCallback
        openImageChooser()
        return true
    }

    override fun onGeolocationPermissionsShowPrompt(
        origin: String?,
        callback: GeolocationPermissions.Callback
    ) {
        callback.invoke(origin, true, false)
    }

    private fun openImageChooser() {
        try {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = Constants.TYPE_IMAGE
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            activity.startActivityForResult(intent, 100)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private var mFilePathCallback: ValueCallback<Array<Uri?>?>? = null
        fun handleUploadMessages(resultCode: Int, data: Intent?) {
            var results: Array<Uri?>? = null
            if (resultCode == 0) {
                mFilePathCallback!!.onReceiveValue(null)
                mFilePathCallback = null
                return
            }
            try {
                if (resultCode != Activity.RESULT_OK) {
                    results = null
                } else {
                    if (data != null) {
                        val dataString = data.dataString
                        val clipData = data.clipData
                        if (clipData != null) {
                            results = arrayOfNulls(clipData.itemCount)
                            for (i in 0 until clipData.itemCount) {
                                val item = clipData.getItemAt(i)

                                results[i] = item.uri
                            }
                        }
                        if (dataString != null) {
                            results = arrayOf(Uri.parse(dataString))
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            mFilePathCallback!!.onReceiveValue(results)
            mFilePathCallback = null
        }
    }
}


