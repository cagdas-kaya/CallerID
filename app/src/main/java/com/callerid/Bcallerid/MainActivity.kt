package com.callerid.Bcallerid

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.provider.Settings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.callerid.Bcallerid.Utils.Constants
import com.callerid.Bcallerid.Utils.MyWebChromeClient
import com.callerid.Bcallerid.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()
        createWebView()
    }

    private fun createWebView() {
        val homeUrl = intent.getStringExtra(Constants.EXTRA_URL) ?: Constants.URL_HOME
        webView = binding.webView
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        setWebViewClient()
        webView.loadUrl(homeUrl)
        setListeners()

    }

    private fun setListeners() {
        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            val filename = contentDisposition.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //Notify client once download is completed!
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                filename[1]
            )
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(
                applicationContext,
                getString(R.string.downloading_file),  //To notify the Client that the file is being downloaded
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun createWebPagePrint(webView: WebView) {
        val printManager = getSystemService(PRINT_SERVICE) as PrintManager
        val printAdapter = webView.createPrintDocumentAdapter()
        val jobName = getString(R.string.print_document)
        val builder = PrintAttributes.Builder()
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5)
        val printJob = printManager.print(jobName, printAdapter, builder.build())
        if (printJob.isCompleted) {
            Toast.makeText(
                applicationContext,
                getString(R.string.print_complete),
                Toast.LENGTH_LONG
            ).show()
        } else if (printJob.isFailed) {
            Toast.makeText(applicationContext, getString(R.string.print_error), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun setWebViewClient() {
        webView.webChromeClient = MyWebChromeClient(this)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith(Constants.WHATSAPP_PREFIX)) {
                    try {
                        webView.stopLoading()
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        view.context.startActivity(intent)
                        return true
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                } else if (url.startsWith(Constants.TEL_PREFIX)) {
                    webView.stopLoading()
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse(url)
                    view.context.startActivity(intent)
                    return true
                } else if (url.contains(Constants.MAPS_PREFIX)) {
                    try {
                        webView.stopLoading()
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        intent.setClassName(
                            "com.google.android.apps.maps",
                            "com.google.android.maps.MapsActivity"
                        )
                        view.context.startActivity(intent)
                        return true
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url!!.contains(Constants.PRINT_PREFIX)) {
                    createWebPagePrint(webView)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            MyWebChromeClient.handleUploadMessages(resultCode, data)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun checkPermissions() {
        ActivityCompat.requestPermissions(
            this,
            Constants.permissions,
            Constants.PERMISSION_REQUEST_CODE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(
                applicationContext
            )
        ) {
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