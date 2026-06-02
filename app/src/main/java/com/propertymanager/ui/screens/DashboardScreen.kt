@file:OptIn(ExperimentalMaterial3Api::class)
package com.propertymanager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    totalProperties: Int,
    activeLeases: Int,
    totalIncome: Double,
    totalExpenses: Double,
    onPropertyClick: () -> Unit,
    onLeaseClick: () -> Unit,
    onTenantClick: () -> Unit,
    onExpenseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("首页") })
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "物业",
                    value = totalProperties.toString(),
                    icon = Icons.Default.Home,
                    onClick = onPropertyClick,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "租约",
                    value = activeLeases.toString(),
                    icon = Icons.Default.DateRange,
                    onClick = onLeaseClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "总收入",
                    value = "¥${totalIncome}",
                    icon = Icons.Default.AttachMoney,
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary
                )
                StatCard(
                    title = "总支出",
                    value = "¥${totalExpenses}",
                    icon = Icons.Default.Delete,
                    onClick = onExpenseClick,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Quick Actions
            Text("快捷操作", style = MaterialTheme.typography.titleMedium)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { QuickActionItem(Icons.Default.Add, "添加物业", onPropertyClick) }
                item { QuickActionItem(Icons.Default.PersonAdd, "添加租客", onTenantClick) }
                item { QuickActionItem(Icons.Default.AddBox, "添加租约", onLeaseClick) }
                item { QuickActionItem(Icons.Default.ReceiptLong, "添加支出", onExpenseClick) }
                item { QuickActionItem(Icons.Default.PictureAsPdf, "导出报表", { }) }
                item { QuickActionItem(Icons.Default.Settings, "设置", { }) }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = color)
            Spacer(Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, color = color)
            Text(title, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun QuickActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(100.dp)
            .clickable(onClick = onClick)
    ) {
        Card(
            modifier = Modifier.size(56.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
