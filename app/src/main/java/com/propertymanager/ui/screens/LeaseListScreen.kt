package com.propertymanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.propertymanager.data.Lease
import com.propertymanager.data.LeaseStatus

@Composable
fun LeaseListScreen(
    leases: List<Lease>,
    onLeaseClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("租约管理") },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Default.Add, contentDescription = "添加租约")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "添加")
            }
        }
    ) { padding ->
        if (leases.isEmpty()) {
            Box(
                modifier = modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("暂无租约记录")
                }
            }
        } else {
            LazyColumn(
                modifier = modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(leases) { lease ->
                    LeaseCard(lease = lease, onClick = { onLeaseClick(lease.id) })
                }
            }
        }
    }
}

@Composable
fun LeaseCard(
    lease: Lease,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusColor = when (lease.status) {
        LeaseStatus.ACTIVE -> MaterialTheme.colorScheme.primary
        LeaseStatus.EXPIRED -> MaterialTheme.colorScheme.error
        LeaseStatus.TERMINATED -> MaterialTheme.colorScheme.onSurfaceVariant
        LeaseStatus.RENEWED -> MaterialTheme.colorScheme.secondary
    }

    Card(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "租约 #${lease.id}",
                    style = MaterialTheme.typography.titleMedium
                )
                AssistChip(
                    onClick = { },
                    label = { Text(lease.status.name) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = statusColor.copy(alpha = 0.1f),
                        labelColor = statusColor
                    )
                )
            }
            Spacer(Modifier.height(8.dp))
            Text("月租金: ¥${lease.monthlyRent}", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))
            Text("租期: ${lease.startDate} 至 ${lease.endDate}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
