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
import com.propertymanager.data.Lease
import kotlinx.coroutines.launch

@Composable
fun LeaseDetailScreen(
    leaseId: Long = 0,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as PropertyManagerApp
    val dao = app.database.leaseDao()

    var lease by remember { mutableStateOf<Lease?>(null) }

    LaunchedEffect(leaseId) {
        lease = dao.getLeaseById(leaseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("租約詳情") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (lease != null) {
                Text("租約 #${lease!!.id}", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text("月租金：$${lease!!.monthlyRent}", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(4.dp))
                Text("租期：${lease!!.startDate} 至 ${lease!!.endDate}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text("狀態：${lease!!.status}", style = MaterialTheme.typography.bodyMedium)
            } else {
                Text("載入中...")
            }
        }
    }
}