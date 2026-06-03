@file:OptIn(ExperimentalMaterial3Api::class)
package com.propertymanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.propertymanager.PropertyManagerApp
import com.propertymanager.data.Property
import kotlinx.coroutines.launch

@Composable
fun PropertyDetailScreen(
    propertyId: Long = 0,
    onBack: () -> Unit,
    onEdit: () -> Unit = {}
) {
    val context = LocalContext.current
    val app = context.applicationContext as PropertyManagerApp
    val dao = app.database.propertyDao()
    val scope = rememberCoroutineScope()

    var property by remember { mutableStateOf<Property?>(null) }

    LaunchedEffect(propertyId) {
        property = dao.getPropertyById(propertyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(property?.name ?: "物業詳情") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            if (property != null) {
                Text(property!!.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text("地址：${property!!.address}", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(4.dp))
                Text("類型：${property!!.type}", style = MaterialTheme.typography.bodyMedium)
            } else {
                Text("載入中...")
            }
        }
    }
}