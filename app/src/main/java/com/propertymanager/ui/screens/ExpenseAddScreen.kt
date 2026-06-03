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
import com.propertymanager.data.Expense
import com.propertymanager.data.ExpenseCategory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExpenseAddScreen(
    onBack: () -> Unit,
    onSave: () -> Unit = {}
) {
    var expenseName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val context = LocalContext.current
    val app = context.applicationContext as PropertyManagerApp
    val dao = app.database.expenseDao()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("新增支出") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (expenseName.isNotBlank() && amount.isNotBlank()) {
                            scope.launch {
                                val cat = when {
                                    category.contains("維") || category.contains("维修") -> ExpenseCategory.MAINTENANCE
                                    category.contains("管") || category.contains("管理") -> ExpenseCategory.MANAGEMENT
                                    category.contains("水") || category.contains("电") || category.contains("電") -> ExpenseCategory.UTILITY
                                    category.contains("稅") || category.contains("税") -> ExpenseCategory.TAX
                                    category.contains("保") -> ExpenseCategory.INSURANCE
                                    else -> ExpenseCategory.OTHER
                                }
                                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                dao.insert(Expense(
                                    propertyId = 0,
                                    category = cat,
                                    amount = amount.toDoubleOrNull() ?: 0.0,
                                    date = today,
                                    description = expenseName,
                                    notes = note
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
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("支出項目 *", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = expenseName,
                onValueChange = { expenseName = it },
                label = { Text("請輸入支出項目") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("金額 *", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("請輸入金額") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("類別", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("維修/水電/管理/稅/保險/其他") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("備註", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("備註（可選）") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}