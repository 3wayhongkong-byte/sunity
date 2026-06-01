package com.propertymanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RentPaymentDao {
    @Query("SELECT * FROM rent_payments ORDER BY paymentDate DESC")
    fun getAllPayments(): Flow<List<RentPayment>>

    @Query("SELECT * FROM rent_payments WHERE leaseId = :leaseId ORDER BY paymentDate DESC")
    fun getPaymentsByLease(leaseId: Long): Flow<List<RentPayment>>

    @Query("SELECT SUM(amount) FROM rent_payments WHERE leaseId = :leaseId")
    suspend fun getTotalPaid(leaseId: Long): Double?

    @Insert
    suspend fun insert(payment: RentPayment): Long

    @Update
    suspend fun update(payment: RentPayment)

    @Delete
    suspend fun delete(payment: RentPayment)
}
