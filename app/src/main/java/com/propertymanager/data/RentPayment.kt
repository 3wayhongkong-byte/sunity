package com.propertymanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rent_payments")
data class RentPayment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val leaseId: Long,
    val paymentDate: String, // ISO date string
    val amount: Double,
    val paymentMethod: String,
    val notes: String = "",
    val receiptPhoto: String = "", // Photo URI
    val createdAt: Long = System.currentTimeMillis()
)
