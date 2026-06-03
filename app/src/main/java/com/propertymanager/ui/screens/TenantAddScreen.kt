@file:OptIn(ExperimentalMaterial3Api::class)
package com.propertymanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TenantAddScreen(
    onBack: () -> Unit,
    onSave: () -> Unit = {}
) {
    var tenantName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("新增租客") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = onSave) {
                        Icon(Icons.Default.Save, contentDescription = "保存")
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
            OutlinedTextField(
                value = tenantName,
                onValueChange = { tenantName = it },
                label = { Text("請輸入姓名") },
                modifier = Modifier.fillMaxWidth()
            )
            Text("電話", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("請輸入電話") },
                modifier = Modifier.fillMaxWidth()
            )
            Text("電郵", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("請輸入電郵（可選）") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}