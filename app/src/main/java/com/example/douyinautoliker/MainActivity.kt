package com.example.douyinautoliker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.douyinautoliker.service.AutoLikerService
import com.example.douyinautoliker.ui.MainScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onOpenAccessibilitySettings = { openAccessibilitySettings() },
                        onRequestOverlayPermission = { requestOverlayPermission() },
                        onStartService = { config -> startAutoLiker(config) },
                        onStopService = { stopAutoLiker() }
                    )
                }
            }
        }
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        }
    }

    private fun startAutoLiker(config: AutoLikerService.LikeConfig) {
        val intent = Intent(this, AutoLikerService::class.java)
        intent.putExtra("config", config)
        startService(intent)
    }

    private fun stopAutoLiker() {
        val intent = Intent(this, AutoLikerService::class.java)
        stopService(intent)
    }
}
