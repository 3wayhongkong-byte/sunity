package com.propertymanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE isNotified = 0 ORDER BY triggerDate ASC")
    fun getPendingReminders(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getReminderById(id: Long): Reminder?

    @Insert
    suspend fun insert(reminder: Reminder): Long

    @Update
    suspend fun update(reminder: Reminder)

    @Query("UPDATE reminders SET isNotified = 1 WHERE id = :id")
    suspend fun markAsNotified(id: Long)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun delete(id: Long)
}
