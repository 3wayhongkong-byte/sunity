package com.propertymanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties ORDER BY updatedAt DESC")
    fun getAllProperties(): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: Long): Property?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(property: Property): Long

    @Update
    suspend fun update(property: Property)

    @Delete
    suspend fun delete(property: Property)

    @Query("SELECT COUNT(*) FROM properties")
    suspend fun getPropertyCount(): Int
}
