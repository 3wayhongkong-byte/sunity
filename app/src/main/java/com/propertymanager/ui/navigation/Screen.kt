package com.propertymanager.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object PropertyList : Screen("properties")
    object PropertyDetail : Screen("property/{id}") {
        fun createRoute(id: Long) = "property/$id"
    }
    object PropertyEdit : Screen("property/edit/{id}") {
        fun createRoute(id: Long) = "property/edit/$id"
    }
    object PropertyAdd : Screen("property/add")
    object TenantList : Screen("tenants")
    object TenantDetail : Screen("tenant/{id}") {
        fun createRoute(id: Long) = "tenant/$id"
    }
    object TenantAdd : Screen("tenant/add")
    object LeaseList : Screen("leases")
    object LeaseDetail : Screen("lease/{id}") {
        fun createRoute(id: Long) = "lease/$id"
    }
    object LeaseAdd : Screen("lease/add")
    object ExpenseList : Screen("expenses")
    object ExpenseAdd : Screen("expense/add")
    object Reports : Screen("reports")
    object Settings : Screen("settings")
}
