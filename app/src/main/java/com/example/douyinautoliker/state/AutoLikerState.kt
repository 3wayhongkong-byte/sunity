package com.example.douyinautoliker.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AutoLikerStatus {
    IDLE,
    SEARCHING,
    CLICKING,
    SWIPING,
    FINISHED,
    ERROR
}

data class AutoLikerState(
    val isRunning: Boolean = false,
    val status: AutoLikerStatus = AutoLikerStatus.IDLE,
    val likedCount: Int = 0,
    val maxLikes: Int = 50,
    val intervalSeconds: Int = 3,
    val errorMessage: String = "",
    val isAccessibilityEnabled: Boolean = false,
    val isOverlayPermissionGranted: Boolean = false,
)

object AutoLikerStateHolder {
    private val _state = MutableStateFlow(AutoLikerState())
    val state: StateFlow<AutoLikerState> = _state.asStateFlow()

    fun update(transform: AutoLikerState.() -> AutoLikerState) {
        _state.value = _state.value.transform()
    }

    fun reset() {
        _state.value = AutoLikerState(
            isAccessibilityEnabled = _state.value.isAccessibilityEnabled,
            isOverlayPermissionGranted = _state.value.isOverlayPermissionGranted,
        )
    }
}
