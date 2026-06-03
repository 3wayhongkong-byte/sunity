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
import com.propertymanager.data.Tenant
import kotlinx.coroutines.launch

@Composable
fun TenantDetailScreen(
    tenantId: Long = 0,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as PropertyManagerApp
    val dao = app.database.tenantDao()

    var tenant by remember { mutableStateOf<Tenant?>(null) }

    LaunchedEffect(tenantId) {
        tenant = dao.getTenantById(tenantId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tenant?.name ?: "租客詳情") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (tenant != null) {
                Text(tenant!!.name, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text("電話：${tenant!!.phone}", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(4.dp))
                Text("電郵：${tenant!!.email}", style = MaterialTheme.typography.bodyMedium)
            } else {
                Text("載入中...")
            }
        }
    }
}