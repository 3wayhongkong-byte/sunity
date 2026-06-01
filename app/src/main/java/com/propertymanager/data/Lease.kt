package com.propertymanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "leases")
data class Lease(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propertyId: Long,
    val tenantId: Long,
    val startDate: String, // ISO date string
    val endDate: String, // ISO date string
    val monthlyRent: Double,
    val deposit: Double,
    val paymentMethod: String = "Bank Transfer",
    val paymentDay: Int = 1, // 每月几号付款
    val status: LeaseStatus = LeaseStatus.ACTIVE,
    val terms: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class LeaseStatus {
    ACTIVE,
    EXPIRED,
    TERMINATED,
    RENEWED
}
