package com.propertymanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaseDao {
    @Query("SELECT * FROM leases ORDER BY startDate DESC")
    fun getAllLeases(): Flow<List<Lease>>

    @Query("SELECT * FROM leases WHERE id = :id")
    suspend fun getLeaseById(id: Long): Lease?

    @Query("SELECT * FROM leases WHERE propertyId = :propertyId")
    fun getLeasesByProperty(propertyId: Long): Flow<List<Lease>>

    @Query("SELECT * FROM leases WHERE tenantId = :tenantId")
    fun getLeasesByTenant(tenantId: Long): Flow<List<Lease>>

    @Query("SELECT * FROM leases WHERE status = 'ACTIVE' AND endDate < :date ORDER BY endDate ASC")
    fun getExpiringLeases(date: String): Flow<List<Lease>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lease: Lease): Long

    @Update
    suspend fun update(lease: Lease)

    @Delete
    suspend fun delete(lease: Lease)
}
