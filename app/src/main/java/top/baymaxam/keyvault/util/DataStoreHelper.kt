package top.baymaxam.keyvault.util

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStoreHelper
 * @author John
 * @since 07 2月 2025
 */
class DataStoreHelper(context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    private val dataStore = context.dataStore

    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    fun <T> get(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { it[key] }
    }

    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit { it.remove(key) }
    }
}