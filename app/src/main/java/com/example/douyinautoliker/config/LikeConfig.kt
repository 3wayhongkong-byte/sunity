package com.example.douyinautoliker.config

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LikeConfig(
    val intervalSeconds: Int = 3,
    val maxLikes: Int = 50,
    val likeButtonDescription: String = "赞",
    val likeButtonId: String = "",
    val swipeUpDurationMs: Long = 800L,
    val gestureDelayMs: Long = 500L,
) : Parcelable
