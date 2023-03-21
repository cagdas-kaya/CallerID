package com.callerid.Bcallerid

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.callerid.Bcallerid.Utils.Constants
import com.callerid.Bcallerid.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()
    }
    private fun checkPermissions() {
        val permissions = arrayOf<String>(
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_CALL_LOG,
            android.Manifest.permission.SYSTEM_ALERT_WINDOW,
            android.Manifest.permission.SYSTEM_ALERT_WINDOW,
        )
        ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(applicationContext)) {
            showOverlayDialog()
        }
    }

    private fun showOverlayDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.overlay_dialog_title))
        builder.setMessage(getString(R.string.overlay_dialog_text))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.overlay_dialog_button)) { dialog, _ ->
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${packageName}")
                )
                startActivity(intent)
                dialog.dismiss()
            }

        val alert = builder.create()
        alert.show()
    }
}