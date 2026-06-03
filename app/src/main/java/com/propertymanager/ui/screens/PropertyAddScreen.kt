@file:OptIn(ExperimentalMaterial3Api::class)
package com.propertymanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.propertymanager.PropertyManagerApp
import com.propertymanager.data.Property
import com.propertymanager.data.PropertyType
import kotlinx.coroutines.launch

@Composable
fun PropertyAddScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var propertyName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var propertyType by remember { mutableStateOf("") }

    val context = LocalContext.current
    val app = context.applicationContext as PropertyManagerApp
    val dao = app.database.propertyDao()
    val scope = rememberCoroutineScope()

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
                    IconButton(onClick = {
                        if (propertyName.isNotBlank()) {
                            scope.launch {
                                dao.insert(Property(
                                    name = propertyName,
                                    address = address,
                                    type = when {
                                        propertyType.contains("商") -> PropertyType.COMMERCIAL
                                        propertyType.contains("混") -> PropertyType.MIXED
                                        else -> PropertyType.RESIDENTIAL
                                    },
                                    area = 0.0
                                ))
                                onSave()
                            }
                        }
                    }) {
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
            Text("物業名稱 *", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = propertyName,
                onValueChange = { propertyName = it },
                label = { Text("請輸入物業名稱") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Text("地址", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("請輸入地址") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Text("類型", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = propertyType,
                onValueChange = { propertyType = it },
                label = { Text("住宅 / 商業 / 混合") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}