package com.example.douyinautoliker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.douyinautoliker.service.AutoLikerService
import com.example.douyinautoliker.state.AutoLikerStateHolder
import com.example.douyinautoliker.state.AutoLikerStatus
import kotlinx.coroutines.flow.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onOpenAccessibilitySettings: () -> Unit,
    onRequestOverlayPermission: () -> Unit,
    onStartService: (AutoLikerService.LikeConfig) -> Unit,
    onStopService: () -> Unit
) {
    val state by AutoLikerStateHolder.state.collectAsState()
    val context = LocalContext.current

    var intervalText by remember { mutableStateOf("3") }
    var maxLikesText by remember { mutableStateOf("50") }
    var likeButtonDesc by remember { mutableStateOf("赞") }

    val hasPostNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
    } else true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "抖音自動點贊",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
        )

        Text(
            text = "Auto Liker",
            fontSize = 14.sp,
            color = Color(0xFF8A8A9A),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (state.isRunning) Color(0xFF1B4332) else Color(0xFF2D2D44)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (state.isRunning) "⚡ 運行中" else "💤 待機中",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.isRunning) Color(0xFF52B788) else Color(0xFF8A8A9A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${state.likedCount}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B6B)
                        )
                        Text("已點贊", fontSize = 12.sp, color = Color(0xFF8A8A9A))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${state.maxLikes}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6C63FF)
                        )
                        Text("目標", fontSize = 12.sp, color = Color(0xFF8A8A9A))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress bar
                if (state.maxLikes > 0) {
                    LinearProgressIndicator(
                        progress = { state.likedCount.toFloat() / state.maxLikes },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = Color(0xFF6C63FF),
                        trackColor = Color(0xFF3D3D5C),
                    )
                }

                if (state.errorMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⚠️ ${state.errorMessage}",
                        fontSize = 12.sp,
                        color = Color(0xFFFF6B6B)
                    )
                }

                if (state.status == AutoLikerStatus.FINISHED) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "✅ 完成！",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF52B788)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Config Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D44))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "⚙️ 設定",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = intervalText,
                    onValueChange = { intervalText = it.filter { c -> c.isDigit() } },
                    label = { Text("點贊間隔（秒）") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6C63FF),
                        unfocusedBorderColor = Color(0xFF3D3D5C),
                        focusedLabelColor = Color(0xFF6C63FF),
                        unfocusedLabelColor = Color(0xFF8A8A9A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = maxLikesText,
                    onValueChange = { maxLikesText = it.filter { c -> c.isDigit() } },
                    label = { Text("最大點贊數") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6C63FF),
                        unfocusedBorderColor = Color(0xFF3D3D5C),
                        focusedLabelColor = Color(0xFF6C63FF),
                        unfocusedLabelColor = Color(0xFF8A8A9A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = likeButtonDesc,
                    onValueChange = { likeButtonDesc = it },
                    label = { Text("點贊按鈕描述詞") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6C63FF),
                        unfocusedBorderColor = Color(0xFF3D3D5C),
                        focusedLabelColor = Color(0xFF6C63FF),
                        unfocusedLabelColor = Color(0xFF8A8A9A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Permissions Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D44))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "🔐 權限",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                PermissionRow(
                    label = "無障礙服務",
                    isGranted = state.isAccessibilityEnabled,
                    onClick = onOpenAccessibilitySettings
                )

                PermissionRow(
                    label = "懸浮窗權限",
                    isGranted = state.isOverlayPermissionGranted,
                    onClick = onRequestOverlayPermission
                )

                if (!hasPostNotificationPermission) {
                    PermissionRow(
                        label = "通知權限",
                        isGranted = false,
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                onRequestOverlayPermission()
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Start/Stop Button
        Button(
            onClick = {
                if (state.isRunning) {
                    onStopService()
                } else {
                    val config = AutoLikerService.LikeConfig(
                        intervalSeconds = intervalText.toIntOrNull() ?: 3,
                        maxLikes = maxLikesText.toIntOrNull() ?: 50,
                        likeButtonDescription = likeButtonDesc
                    )
                    onStartService(config)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (state.isRunning) Color(0xFFE74C3C) else Color(0xFF6C63FF)
            ),
            enabled = state.isAccessibilityEnabled
        ) {
            Icon(
                imageVector = if (state.isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (state.isRunning) "停止" else "開始點贊",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (!state.isAccessibilityEnabled) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "⚠️ 請先開啟無障礙服務權限",
                fontSize = 12.sp,
                color = Color(0xFFFF6B6B)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun PermissionRow(
    label: String,
    isGranted: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White
        )

        if (isGranted) {
            Text(
                text = "✅ 已開啟",
                fontSize = 14.sp,
                color = Color(0xFF52B788)
            )
        } else {
            TextButton(onClick = onClick) {
                Text(
                    text = "前往開啟",
                    color = Color(0xFF6C63FF),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
