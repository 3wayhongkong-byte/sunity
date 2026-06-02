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
import com.propertymanager.ui.screens.TenantListScreen
import com.propertymanager.ui.screens.LeaseListScreen
import com.propertymanager.ui.screens.ExpenseListScreen
import com.propertymanager.ui.theme.PropertyManagerTheme
import androidx.navigation.compose.*

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
        composable(Screen.PropertyList.route) {
            PropertyListScreen(
                properties = emptyList(),
                onPropertyClick = { id -> navController.navigate(Screen.PropertyDetail.createRoute(id)) },
                onAddClick = { navController.navigate(Screen.PropertyAdd.route) }
            )
        }
        composable(Screen.TenantList.route) {
            TenantListScreen(
                tenants = emptyList(),
                onTenantClick = { id -> },
                onAddClick = { navController.navigate(Screen.TenantAdd.route) }
            )
        }
        composable(Screen.LeaseList.route) {
            LeaseListScreen(
                leases = emptyList(),
                onLeaseClick = { id -> },
                onAddClick = { navController.navigate(Screen.LeaseAdd.route) }
            )
        }
        composable(Screen.ExpenseList.route) {
            ExpenseListScreen(
                expenses = emptyList(),
                onExpenseClick = { id -> },
                onAddClick = { navController.navigate(Screen.ExpenseAdd.route) }
            )
        }
    }
}
