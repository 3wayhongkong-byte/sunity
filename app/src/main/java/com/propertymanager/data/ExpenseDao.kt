package com.propertymanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE propertyId = :propertyId ORDER BY date DESC")
    fun getExpensesByProperty(propertyId: Long): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE propertyId = :propertyId AND strftime('%Y', date) = :year")
    suspend fun getYearlyExpensesByProperty(propertyId: Long, year: String): Double?

    @Query("SELECT strftime('%Y-%m', date) as month, SUM(amount) as total FROM expenses GROUP BY month ORDER BY month DESC")
    fun getMonthlyExpenseSummary(): Flow<List<ExpenseSummary>>

    @Insert
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)
}

data class ExpenseSummary(
    val month: String,
    val total: Double
)
