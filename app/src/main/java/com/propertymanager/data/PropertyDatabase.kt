package com.propertymanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Property::class,
        Tenant::class,
        Lease::class,
        RentPayment::class,
        Expense::class,
        Reminder::class
    ],
    version = 2,
    exportSchema = false
)
abstract class PropertyDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun tenantDao(): TenantDao
    abstract fun leaseDao(): LeaseDao
    abstract fun rentPaymentDao(): RentPaymentDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: PropertyDatabase? = null

        fun getDatabase(context: Context): PropertyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PropertyDatabase::class.java,
                    "property_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
