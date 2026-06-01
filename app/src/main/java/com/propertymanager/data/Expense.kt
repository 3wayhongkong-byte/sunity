package com.propertymanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propertyId: Long,
    val category: ExpenseCategory,
    val amount: Double,
    val date: String, // ISO date string
    val description: String,
    val receiptPhoto: String = "", // Photo URI
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class ExpenseCategory {
    MAINTENANCE,      // 维修费
    MANAGEMENT,       // 管理费
    TAX,              // 税费
    UTILITY,          // 水电费
    INSURANCE,        // 保险
    OTHER             // 其他
}
