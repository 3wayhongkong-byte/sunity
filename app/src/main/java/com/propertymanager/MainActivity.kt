package com.propertymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.propertymanager.ui.navigation.Screen
import com.propertymanager.ui.screens.*
import com.propertymanager.ui.theme.PropertyManagerTheme
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PropertyManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PropertyManagerNavHost()
                }
            }
        }
    }
}

@Composable
fun PropertyManagerNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as PropertyManagerApp
    val db = app.database

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        // Dashboard
        composable(Screen.Dashboard.route) {
            val properties by db.propertyDao().getAllProperties().collectAsState(initial = emptyList())
            val leases by db.leaseDao().getAllLeases().collectAsState(initial = emptyList())

            DashboardScreen(
                totalProperties = properties.size,
                activeLeases = leases.count { it.status.name == "ACTIVE" },
                totalIncome = leases.sumOf { it.monthlyRent },
                totalExpenses = 0.0,
                onPropertyClick = { navController.navigate(Screen.PropertyList.route) },
                onLeaseClick = { navController.navigate(Screen.LeaseList.route) },
                onTenantClick = { navController.navigate(Screen.TenantList.route) },
                onExpenseClick = { navController.navigate(Screen.ExpenseList.route) }
            )
        }

        // Property
        composable(Screen.PropertyList.route) {
            val properties by db.propertyDao().getAllProperties().collectAsState(initial = emptyList())
            PropertyListScreen(
                properties = properties,
                onPropertyClick = { id -> navController.navigate(Screen.PropertyDetail.createRoute(id)) },
                onAddClick = { navController.navigate(Screen.PropertyAdd.route) }
            )
        }
        composable(Screen.PropertyAdd.route) {
            PropertyAddScreen(
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
        composable(
            Screen.PropertyDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            PropertyDetailScreen(
                propertyId = id,
                onBack = { navController.popBackStack() }
            )
        }

        // Tenant
        composable(Screen.TenantList.route) {
            val tenants by db.tenantDao().getAllTenants().collectAsState(initial = emptyList())
            TenantListScreen(
                tenants = tenants,
                onTenantClick = { id -> navController.navigate(Screen.TenantDetail.createRoute(id)) },
                onAddClick = { navController.navigate(Screen.TenantAdd.route) }
            )
        }
        composable(Screen.TenantAdd.route) {
            TenantAddScreen(
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
        composable(
            Screen.TenantDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            TenantDetailScreen(
                tenantId = id,
                onBack = { navController.popBackStack() }
            )
        }

        // Lease
        composable(Screen.LeaseList.route) {
            val leases by db.leaseDao().getAllLeases().collectAsState(initial = emptyList())
            LeaseListScreen(
                leases = leases,
                onLeaseClick = { id -> navController.navigate(Screen.LeaseDetail.createRoute(id)) },
                onAddClick = { navController.navigate(Screen.LeaseAdd.route) }
            )
        }
        composable(Screen.LeaseAdd.route) {
            LeaseAddScreen(
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
        composable(
            Screen.LeaseDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            LeaseDetailScreen(
                leaseId = id,
                onBack = { navController.popBackStack() }
            )
        }

        // Expense
        composable(Screen.ExpenseList.route) {
            val expenses by db.expenseDao().getAllExpenses().collectAsState(initial = emptyList())
            ExpenseListScreen(
                expenses = expenses,
                onExpenseClick = { id -> },
                onAddClick = { navController.navigate(Screen.ExpenseAdd.route) }
            )
        }
        composable(Screen.ExpenseAdd.route) {
            ExpenseAddScreen(
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }

        // Settings
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}