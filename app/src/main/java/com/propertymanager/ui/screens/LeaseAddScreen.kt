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
fun LeaseAddScreen(
    onBack: () -> Unit,
    onSave: () -> Unit = {}
) {
    var leaseName by remember { mutableStateOf("") }
    var tenantName by remember { mutableStateOf("") }
    var rentAmount by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("新增租約") },
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
            Text("租約名稱", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = leaseName,
                onValueChange = { leaseName = it },
                label = { Text("請輸入租約名稱") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("租客姓名", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = tenantName,
                onValueChange = { tenantName = it },
                label = { Text("請輸入租客姓名") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("租金金額", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = rentAmount,
                onValueChange = { rentAmount = it },
                label = { Text("請輸入租金金額") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("開始日期", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = { Text("YYYY-MM-DD") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("結束日期", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = endDate,
                onValueChange = { endDate = it },
                label = { Text("YYYY-MM-DD") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}