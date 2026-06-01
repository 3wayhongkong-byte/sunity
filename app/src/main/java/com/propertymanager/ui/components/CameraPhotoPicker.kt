package com.propertymanager.ui.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File

@Composable
fun CameraPhotoPicker(
    onPhotoTaken: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageCaptureUri = remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageCaptureUri.value?.let { uri ->
                onPhotoTaken(uri)
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onPhotoTaken(it) }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Camera button
        FloatingActionButton(
            onClick = {
                val photoFile = createPhotoFile(context)
                imageCaptureUri.value = Uri.fromFile(photoFile)
                takePictureLauncher.launch(imageCaptureUri.value)
            },
            modifier = Modifier.size(56.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.CameraAlt, contentDescription = "拍照")
        }
        
        Spacer(Modifier.height(8.dp))
        
        // Gallery button
        OutlinedButton(
            onClick = { galleryLauncher.launch("image/*") },
            modifier = Modifier.width(120.dp)
        ) {
            Icon(Icons.Default.Image, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("从相册选择")
        }
    }
}

@Composable
fun PhotoPreview(
    photoUri: Uri?,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (photoUri == null) return

    Card(
        modifier = modifier.size(120.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(modifier.fillMaxSize()) {
            AsyncImage(
                model = photoUri,
                contentDescription = "照片",
                modifier = Modifier.fillMaxSize()
            )
            
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        CircleShape
                    )
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "删除",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

private fun createPhotoFile(context: Context): File {
    val photoDir = File(context.externalCacheDir, "photos")
    photoDir.mkdirs()
    val timeStamp = System.currentTimeMillis()
    return File(photoDir, "IMG_$timeStamp.jpg")
}
