package com.propertymanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class Property(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val address: String,
    val type: PropertyType, // Residential, Commercial, Mixed
    val area: Double, // 面积 (sqm)
    val purchasePrice: Double = 0.0,
    val purchaseDate: String = "",
    val photos: String = "", // JSON array of photo URIs
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class PropertyType {
    RESIDENTIAL,
    COMMERCIAL,
    MIXED
}
