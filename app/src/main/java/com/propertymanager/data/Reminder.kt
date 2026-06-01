package com.propertymanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: ReminderType,
    val relatedId: Long, // leaseId, propertyId, etc.
    val title: String,
    val message: String,
    val triggerDate: String, // ISO date string
    val isNotified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ReminderType {
    LEASE_EXPIRY,       // 租约到期
    RENT_DUE,           // 租金到期
    PAYMENT_OVERDUE,    // 逾期付款
    MAINTENANCE,        // 维修提醒
    OTHER               // 其他
}
