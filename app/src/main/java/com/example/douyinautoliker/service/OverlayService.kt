package com.example.douyinautoliker.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.example.douyinautoliker.MainActivity
import com.example.douyinautoliker.R
import com.example.douyinautoliker.state.AutoLikerStateHolder
import kotlinx.coroutines.flow.first

class OverlayService : Service() {

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var params: WindowManager.LayoutParams? = null

    companion object {
        var isRunning = false
            private set
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = "douyin_autoliker"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "點贊服務",
                NotificationManager.IMPORTANCE_LOW
            )
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)

            val notification = Notification.Builder(this, channelId)
                .setContentTitle("抖音自動點贊")
                .setContentText("浮窗已啟動")
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setOngoing(true)
                .build()

            startForeground(1, notification)
        }

        showOverlay()
        return START_STICKY
    }

    private fun showOverlay() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        overlayView = inflater.inflate(R.layout.overlay_bubble, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 200

        this.params = params
        windowManager?.addView(overlayView, params)

        overlayView?.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            private var isDragging = false

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = this@OverlayService.params!!.x
                        initialY = this@OverlayService.params!!.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        isDragging = false
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        this@OverlayService.params!!.x = initialX + (event.rawX - initialTouchX).toInt()
                        this@OverlayService.params!!.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager?.updateViewLayout(overlayView, this@OverlayService.params)
                        isDragging = true
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (!isDragging) {
                            val intent = Intent(this@OverlayService, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        return true
                    }
                }
                return false
            }
        })

        updateOverlayText()
    }

    fun updateOverlayText() {
        overlayView?.let { view ->
            val statusText = view.findViewById<TextView>(R.id.overlay_status)
            val likedText = view.findViewById<TextView>(R.id.overlay_liked)
            val state = kotlinx.coroutines.runBlocking {
                AutoLikerStateHolder.state.first()
            }
            statusText?.text = if (state.isRunning) "運行中" else "待機"
            likedText?.text = "贊: ${state.likedCount}/${state.maxLikes}"
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        isRunning = false
        overlayView?.let { windowManager?.removeView(it) }
        super.onDestroy()
    }
}
