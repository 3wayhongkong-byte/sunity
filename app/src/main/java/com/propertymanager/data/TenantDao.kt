package com.propertymanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TenantDao {
    @Query("SELECT * FROM tenants ORDER BY updatedAt DESC")
    fun getAllTenants(): Flow<List<Tenant>>

    @Query("SELECT * FROM tenants WHERE id = :id")
    suspend fun getTenantById(id: Long): Tenant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tenant: Tenant): Long

    @Update
    suspend fun update(tenant: Tenant)

    @Delete
    suspend fun delete(tenant: Tenant)

    @Query("SELECT * FROM tenants WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%'")
    fun searchTenants(query: String): Flow<List<Tenant>>
}
