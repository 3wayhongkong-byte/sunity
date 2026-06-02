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
fun PropertyAddScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("新增物業") },
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("物業名稱", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("請輸入物業名稱") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Text("地址", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("請輸入地址") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Text("類型", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("住宅/商業/工業") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}