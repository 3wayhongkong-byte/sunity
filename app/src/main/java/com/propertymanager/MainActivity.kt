package com.propertymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import com.propertymanager.ui.navigation.Screen
import com.propertymanager.ui.screens.DashboardScreen
import com.propertymanager.ui.screens.PropertyListScreen
import com.propertymanager.ui.screens.PropertyDetailScreen
import com.propertymanager.ui.screens.PropertyAddScreen
import com.propertymanager.ui.screens.TenantListScreen
import com.propertymanager.ui.screens.TenantDetailScreen
import com.propertymanager.ui.screens.TenantAddScreen
import com.propertymanager.ui.screens.LeaseListScreen
import com.propertymanager.ui.screens.LeaseDetailScreen
import com.propertymanager.ui.screens.LeaseAddScreen
import com.propertymanager.ui.screens.ExpenseListScreen
import com.propertymanager.ui.screens.ExpenseAddScreen
import com.propertymanager.ui.screens.SettingsScreen
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

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        // Dashboard
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                totalProperties = 0,
                activeLeases = 0,
                totalIncome = 0.0,
                totalExpenses = 0.0,
                onPropertyClick = { navController.navigate(Screen.PropertyList.route) },
                onLeaseClick = { navController.navigate(Screen.LeaseList.route) },
                onTenantClick = { navController.navigate(Screen.TenantList.route) },
                onExpenseClick = { navController.navigate(Screen.ExpenseList.route) }
            )
        }

        // Property
        composable(Screen.PropertyList.route) {
            PropertyListScreen(
                properties = emptyList(),
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
            TenantListScreen(
                tenants = emptyList(),
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
            LeaseListScreen(
                leases = emptyList(),
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
            ExpenseListScreen(
                expenses = emptyList(),
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