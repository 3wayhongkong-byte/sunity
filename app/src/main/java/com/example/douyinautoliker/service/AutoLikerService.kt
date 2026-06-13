package com.example.douyinautoliker.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.douyinautoliker.state.AutoLikerStatus
import com.example.douyinautoliker.state.AutoLikerStateHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class AutoLikerService : AccessibilityService() {

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    companion object {
        var instance: AutoLikerService? = null
            private set
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        AutoLikerStateHolder.update { copy(isAccessibilityEnabled = true) }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 不需要即時處理，job loop 會處理
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        instance = null
        stop()
        super.onDestroy()
    }

    fun start(config: LikeConfig) {
        stop()
        AutoLikerStateHolder.update {
            copy(
                isRunning = true,
                status = AutoLikerStatus.IDLE,
                likedCount = 0,
                maxLikes = config.maxLikes,
                intervalSeconds = config.intervalSeconds,
                errorMessage = ""
            )
        }
        job = scope.launch { autoLikeLoop(config) }
    }

    fun stop() {
        job?.cancel()
        job = null
        AutoLikerStateHolder.update {
            copy(isRunning = false, status = AutoLikerStatus.IDLE)
        }
    }

    private suspend fun autoLikeLoop(config: LikeConfig) {
        val intervalMs = config.intervalSeconds * 1000L
        var consecutiveEmpty = 0

        while (isActive) {
            val currentState = AutoLikerStateHolder.state.first()
            if (currentState.likedCount >= currentState.maxLikes) {
                AutoLikerStateHolder.update { copy(status = AutoLikerStatus.FINISHED, isRunning = false) }
                return
            }

            AutoLikerStateHolder.update { copy(status = AutoLikerStatus.SEARCHING) }
            delay(config.gestureDelayMs)

            val root = rootInActiveWindow ?: continue
            val likeButton = findLikeButton(root, config)

            if (likeButton != null) {
                AutoLikerStateHolder.update { copy(status = AutoLikerStatus.CLICKING) }
                likeButton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                likeButton.recycle()
                consecutiveEmpty = 0

                AutoLikerStateHolder.update { copy(likedCount = likedCount + 1) }

                delay(config.gestureDelayMs * 2)

                AutoLikerStateHolder.update { copy(status = AutoLikerStatus.SWIPING) }
                performSwipeUp(config.swipeUpDurationMs)

                delay(intervalMs)
            } else {
                root.recycle()
                consecutiveEmpty++
                if (consecutiveEmpty >= 10) {
                    AutoLikerStateHolder.update {
                        copy(
                            status = AutoLikerStatus.ERROR,
                            errorMessage = "多次找不到點贊按鈕，請確認抖音已打開",
                            isRunning = false
                        )
                    }
                    return
                }
                // 未找到就滑動
                performSwipeUp(config.swipeUpDurationMs)
                delay(intervalMs)
            }
        }
    }

    private fun findLikeButton(root: AccessibilityNodeInfo, config: LikeConfig): AccessibilityNodeInfo? {
        // Method 1: Search by content description (Chinese)
        val descList = listOf(
            config.likeButtonDescription,
            "赞", "点赞", "like", "Like",
            "未点赞", "点赞的按钮", "给TA点赞"
        )
        for (desc in descList) {
            root.findAccessibilityNodeInfosByText(desc)?.forEach { node ->
                if (node.isClickable && node.isVisibleToUser) {
                    val bounds = Rect()
                    node.getBoundsInScreen(bounds)
                    if (bounds.width() > 0 && bounds.height() > 0) {
                        return node
                    }
                }
            }
        }

        // Method 2: Search by content description exact match
        for (desc in descList) {
            root.findAccessibilityNodeInfosByText(desc)?.forEach { node ->
                if (node.contentDescription?.toString()?.contains(desc, ignoreCase = true) == true
                    && node.isVisibleToUser) {
                    return node
                }
            }
        }

        // Method 3: Search by view ID
        if (config.likeButtonId.isNotBlank()) {
            root.findAccessibilityNodeInfosByViewId(config.likeButtonId)?.forEach { node ->
                if (node.isVisibleToUser) return node
            }
        }

        return null
    }

    private fun performSwipeUp(durationMs: Long) {
        val display = resources.displayMetrics
        val width = display.widthPixels
        val height = display.heightPixels

        val startX = (width / 2).toFloat()
        val startY = (height * 0.7f).toFloat()
        val endY = (height * 0.3f).toFloat()

        val path = Path().apply {
            moveTo(startX, startY)
            lineTo(startX, endY)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, durationMs))
            .build()

        dispatchGesture(gesture, null, null)
    }

    data class LikeConfig(
        val intervalSeconds: Int = 3,
        val maxLikes: Int = 50,
        val likeButtonDescription: String = "赞",
        val likeButtonId: String = "",
        val swipeUpDurationMs: Long = 800L,
        val gestureDelayMs: Long = 500L,
    )
}
