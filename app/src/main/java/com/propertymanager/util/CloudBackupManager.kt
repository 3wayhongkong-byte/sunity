package com.propertymanager.util

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.google.firebase.ktx.Firebase as KtxFirebase
import com.propertymanager.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File

class CloudBackupManager(private val context: Context) {
    
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    
    companion object {
        private const val BACKUP_COLLECTION = "backups"
        private const val PROPERTIES_COLLECTION = "properties"
        private const val TENANTS_COLLECTION = "tenants"
        private const val LEASES_COLLECTION = "leases"
        private const val PAYMENTS_COLLECTION = "rent_payments"
        private const val EXPENSES_COLLECTION = "expenses"
        private const val REMINDERS_COLLECTION = "reminders"
    }
    
    suspend fun backupAllData(userId: String): Result<BackupResult> = withContext(Dispatchers.IO) {
        try {
            val database = PropertyDatabase.getDatabase(context)
            
            // Get all data
            val properties = database.propertyDao().getAllProperties().first()
            val tenants = database.tenantDao().getAllTenants().first()
            val leases = database.leaseDao().getAllLeases().first()
            val payments = database.rentPaymentDao().getAllPayments().first()
            val expenses = database.expenseDao().getAllExpenses().first()
            val reminders = database.reminderDao().getPendingReminders().first()
            
            // Create backup metadata
            val backupId = java.util.UUID.randomUUID().toString()
            val timestamp = System.currentTimeMillis()
            
            val backupMetadata = hashMapOf(
                "backupId" to backupId,
                "userId" to userId,
                "timestamp" to timestamp,
                "propertyCount" to properties.size,
                "tenantCount" to tenants.size,
                "leaseCount" to leases.size,
                "version" to "1.0"
            )
            
            // Upload metadata
            db.collection(BACKUP_COLLECTION)
                .document(userId)
                .collection("metadata")
                .document(backupId)
                .set(backupMetadata)
            
            // Upload data collections
            uploadCollection(PROPERTIES_COLLECTION, userId, backupId, properties.map { it.toMap() })
            uploadCollection(TENANTS_COLLECTION, userId, backupId, tenants.map { it.toMap() })
            uploadCollection(LEASES_COLLECTION, userId, backupId, leases.map { it.toMap() })
            uploadCollection(PAYMENTS_COLLECTION, userId, backupId, payments.map { it.toMap() })
            uploadCollection(EXPENSES_COLLECTION, userId, backupId, expenses.map { it.toMap() })
            uploadCollection(REMINDERS_COLLECTION, userId, backupId, reminders.map { it.toMap() })
            
            // Update last backup time
            PreferenceManager.setLastBackupTime(context, timestamp)
            
            Result.success(BackupResult(backupId, timestamp, true, null))
        } catch (e: Exception) {
            Result.success(BackupResult("", System.currentTimeMillis(), false, e.message))
        }
    }
    
    private suspend fun <T> uploadCollection(
        collectionName: String,
        userId: String,
        backupId: String,
        items: List<Map<String, Any>>
    ) {
        items.chunked(100).forEach { chunk ->
            val batch = db.batch()
            chunk.forEachIndexed { index, item ->
                val docRef = db.collection(collectionName)
                    .document(userId)
                    .collection(backupId)
                    .document()
                batch.set(docRef, item + mapOf("backupId" to backupId))
            }
            batch.commit().await()
        }
    }
    
    suspend fun restoreFromBackup(userId: String, backupId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val database = PropertyDatabase.getDatabase(context)
            
            // Get backup metadata
            val metadata = db.collection(BACKUP_COLLECTION)
                .document(userId)
                .collection("metadata")
                .document(backupId)
                .get()
                .await()
            
            if (!metadata.exists()) {
                return@withContext Result.failure(Exception("备份不存在"))
            }
            
            // Restore each collection
            restoreCollection(PROPERTIES_COLLECTION, userId, backupId, database.propertyDao())
            restoreCollection(TENANTS_COLLECTION, userId, backupId, database.tenantDao())
            restoreCollection(LEASES_COLLECTION, userId, backupId, database.leaseDao())
            restoreCollection(PAYMENTS_COLLECTION, userId, backupId, database.rentPaymentDao())
            restoreCollection(EXPENSES_COLLECTION, userId, backupId, database.expenseDao())
            restoreCollection(REMINDERS_COLLECTION, userId, backupId, database.reminderDao())
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun <T> restoreCollection(
        collectionName: String,
        userId: String,
        backupId: String,
        dao: Any
    ) {
        // Implementation depends on DAO type
        // This is a simplified version
    }
    
    suspend fun uploadPhoto(userId: String, backupId: String, uri: android.net.Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val ref = storage.reference
                .child("users/$userId/photos")
                .child("$backupId/${uri.lastPathSegment}")
            
            val uploadTask = ref.putFile(uri)
            val downloadUrl = uploadTask.await().storage.downloadUrl.await().toString()
            
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class BackupResult(
    val backupId: String,
    val timestamp: Long,
    val success: Boolean,
    val errorMessage: String?
)

// Entity extensions for JSON serialization
fun Property.toMap(): Map<String, Any> = mapOf(
    "id" to id,
    "name" to name,
    "address" to address,
    "type" to type.name,
    "area" to area,
    "purchasePrice" to purchasePrice,
    "purchaseDate" to purchaseDate,
    "photos" to photos,
    "notes" to notes,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt
)

fun Tenant.toMap(): Map<String, Any> = mapOf(
    "id" to id,
    "name" to name,
    "phone" to phone,
    "email" to email,
    "idNumber" to idNumber,
    "nationality" to nationality,
    "emergencyContact" to emergencyContact,
    "emergencyPhone" to emergencyPhone,
    "notes" to notes,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt
)

fun Lease.toMap(): Map<String, Any> = mapOf(
    "id" to id,
    "propertyId" to propertyId,
    "tenantId" to tenantId,
    "startDate" to startDate,
    "endDate" to endDate,
    "monthlyRent" to monthlyRent,
    "deposit" to deposit,
    "paymentMethod" to paymentMethod,
    "paymentDay" to paymentDay,
    "status" to status.name,
    "terms" to terms,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt
)

fun RentPayment.toMap(): Map<String, Any> = mapOf(
    "id" to id,
    "leaseId" to leaseId,
    "paymentDate" to paymentDate,
    "amount" to amount,
    "paymentMethod" to paymentMethod,
    "notes" to notes,
    "receiptPhoto" to receiptPhoto,
    "createdAt" to createdAt
)

fun Expense.toMap(): Map<String, Any> = mapOf(
    "id" to id,
    "propertyId" to propertyId,
    "category" to category.name,
    "amount" to amount,
    "date" to date,
    "description" to description,
    "receiptPhoto" to receiptPhoto,
    "notes" to notes,
    "createdAt" to createdAt
)

fun Reminder.toMap(): Map<String, Any> = mapOf(
    "id" to id,
    "type" to type.name,
    "relatedId" to relatedId,
    "title" to title,
    "message" to message,
    "triggerDate" to triggerDate,
    "isNotified" to isNotified,
    "createdAt" to createdAt
)
