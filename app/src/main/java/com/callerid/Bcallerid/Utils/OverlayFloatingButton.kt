package com.callerid.Bcallerid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import com.callerid.Bcallerid.Services.OverlayFloatingButtonService
import com.callerid.Bcallerid.databinding.FloatingButtonLayoutBinding
import kotlin.math.abs

class OverlayFloatingButton(private val context: Context) {
    private val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var floatingButton: View? = null
    private var startX = 0
    private var startY = 0
    private var initialTouchX = 0
    private var initialTouchY = 0
    private lateinit var binding: FloatingButtonLayoutBinding

    fun show() {
        binding = FloatingButtonLayoutBinding.inflate(LayoutInflater.from(context))
        val layoutParams = getButtonLayoutParams()
        floatingButton = binding.overlayButtonView
        val closeButton = binding.closeButton
        closeButton.setOnClickListener {
            close()
        }
        windowManager.addView(floatingButton, layoutParams)
        setOnTouchListener(layoutParams)
    }

    private fun close() {
        windowManager.removeView(floatingButton)
        context.stopService(Intent(context, OverlayFloatingButtonService::class.java))
    }

    private fun getButtonLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
            flags = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            format = PixelFormat.TRANSPARENT
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.TOP or Gravity.START
            x = startX
            y = startY
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListener(layoutParams: WindowManager.LayoutParams) {
        floatingButton?.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialTouchX = motionEvent.rawX.toInt()
                    initialTouchY = motionEvent.rawY.toInt()
                    startX = layoutParams.x
                    startY = layoutParams.y
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val clickThreshold = 10
                    val diffX = motionEvent.rawX - initialTouchX
                    val diffY = motionEvent.rawY - initialTouchY

                    if (abs(diffX) < clickThreshold && abs(diffY) < clickThreshold) {
                        //Todo click
                    }
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    layoutParams.x =
                        startX + (motionEvent.rawX.toInt() - initialTouchX)
                    layoutParams.y =
                        startY + (motionEvent.rawY.toInt() - initialTouchY)
                    windowManager.updateViewLayout(floatingButton, layoutParams)
                    true
                }
                else -> false
            }
        }
    }
}

