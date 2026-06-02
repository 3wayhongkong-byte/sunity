@file:OptIn(ExperimentalMaterial3Api::class)
package com.propertymanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TenantAddScreen(
    onBack: () -> Unit,
    onSave: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("新增租客") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("租客姓名", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("請輸入姓名") }, modifier = Modifier.fillMaxWidth())
            Text("電話", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("請輸入電話") }, modifier = Modifier.fillMaxWidth())
        }
    }
}