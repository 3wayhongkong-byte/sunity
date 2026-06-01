package com.propertymanager.util

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object PreferenceManager {
    
    private val LANGUAGE_KEY = stringPreferencesKey("language")
    private val BACKUP_ENABLED_KEY = booleanPreferencesKey("backup_enabled")
    private val BACKUP_INTERVAL_KEY = intPreferencesKey("backup_interval_hours")
    private val LAST_BACKUP_KEY = longPreferencesKey("last_backup_timestamp")
    
    // Language
    suspend fun getLanguage(context: Context): String {
        return context.dataStore.data.first()[LANGUAGE_KEY] ?: "zh"
    }
    
    suspend fun setLanguage(context: Context, language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }
    
    // Backup settings
    suspend fun isBackupEnabled(context: Context): Boolean {
        return context.dataStore.data.first()[BACKUP_ENABLED_KEY] ?: false
    }
    
    suspend fun setBackupEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BACKUP_ENABLED_KEY] = enabled
        }
    }
    
    suspend fun getBackupInterval(context: Context): Int {
        return context.dataStore.data.first()[BACKUP_INTERVAL_KEY] ?: 24
    }
    
    suspend fun setBackupInterval(context: Context, hours: Int) {
        context.dataStore.edit { preferences ->
            preferences[BACKUP_INTERVAL_KEY] = hours
        }
    }
    
    suspend fun getLastBackupTime(context: Context): Long {
        return context.dataStore.data.first()[LAST_BACKUP_KEY] ?: 0
    }
    
    suspend fun setLastBackupTime(context: Context, timestamp: Long) {
        context.dataStore.edit { preferences ->
            preferences[LAST_BACKUP_KEY] = timestamp
        }
    }
}
