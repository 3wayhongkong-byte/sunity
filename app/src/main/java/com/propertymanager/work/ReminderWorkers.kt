package com.propertymanager.work

import android.content.Context
import androidx.work.*
import com.propertymanager.data.*
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LeaseExpiryWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val database = PropertyDatabase.getDatabase(appContext)
    private val reminderDao = database.reminderDao()
    private val leaseDao = database.leaseDao()

    override suspend fun doWork(): Result {
        return try {
            checkLeaseExpiries()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private suspend fun checkLeaseExpiries() {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val leases = leaseDao.getAllLeases().first()
        
        for (lease in leases) {
            if (lease.status == LeaseStatus.ACTIVE) {
                val endDate = LocalDate.parse(lease.endDate)
                val daysUntilExpiry = LocalDate.now().until(endDate).days
                
                // 提前 30 天提醒
                if (daysUntilExpiry in 1..30 && daysUntilExpiry % 7 == 0) {
                    val reminder = Reminder(
                        type = ReminderType.LEASE_EXPIRY,
                        relatedId = lease.id,
                        title = "租约即将到期",
                        message = "租约 #${lease.id} 将在 ${daysUntilExpiry} 天后到期",
                        triggerDate = today,
                        isNotified = false
                    )
                    reminderDao.insert(reminder)
                }
                
                // 提前 7 天紧急提醒
                if (daysUntilExpiry in 1..7) {
                    val reminder = Reminder(
                        type = ReminderType.LEASE_EXPIRY,
                        relatedId = lease.id,
                        title = "⚠️ 租约即将到期",
                        message = "租约 #${lease.id} 将在 ${daysUntilExpiry} 天后到期，请尽快处理续约！",
                        triggerDate = today,
                        isNotified = false
                    )
                    reminderDao.insert(reminder)
                }
            }
        }
    }
}

class RentDueWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val database = PropertyDatabase.getDatabase(appContext)
    private val leaseDao = database.leaseDao()
    private val reminderDao = database.reminderDao()

    override suspend fun doWork(): Result {
        return try {
            checkRentDue()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private suspend fun checkRentDue() {
        val today = LocalDate.now()
        val todayDay = today.dayOfMonth
        val leases = leaseDao.getAllLeases().first()
        
        for (lease in leases) {
            if (lease.status == LeaseStatus.ACTIVE && lease.paymentDay == todayDay) {
                val reminder = Reminder(
                    type = ReminderType.RENT_DUE,
                    relatedId = lease.id,
                    title = "租金到期",
                    message = "租约 #${lease.id} 本月租金 ¥${lease.monthlyRent} 到期",
                    triggerDate = today.toString(),
                    isNotified = false
                )
                reminderDao.insert(reminder)
            }
        }
    }
}

fun scheduleReminderWork(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(false)
        .setRequiresCharging(false)
        .setRequiresDeviceIdle(false)
        .setRequiresNetworkType(NetworkType.CONNECTED)
        .build()

    // 每日检查租约到期
    val leaseExpiryWork = PeriodicWorkRequestBuilder<LeaseExpiryWorker>(
        1, java.util.concurrent.TimeUnit.DAYS
    )
        .setConstraints(constraints)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            WorkRequest.MIN_BACKOFF_MILLIS,
            java.util.concurrent.TimeUnit.MILLISECONDS
        )
        .build()

    // 每日检查租金到期
    val rentDueWork = PeriodicWorkRequestBuilder<RentDueWorker>(
        1, java.util.concurrent.TimeUnit.DAYS
    )
        .setConstraints(constraints)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            WorkRequest.MIN_BACKOFF_MILLIS,
            java.util.concurrent.TimeUnit.MILLISECONDS
        )
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "lease_expiry_check",
        ExistingPeriodicWorkPolicy.KEEP,
        leaseExpiryWork
    )

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "rent_due_check",
        ExistingPeriodicWorkPolicy.KEEP,
        rentDueWork
    )
}
