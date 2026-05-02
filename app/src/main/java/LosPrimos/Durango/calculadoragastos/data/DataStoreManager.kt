package LosPrimos.Durango.calculadoragastos.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
val Context.dataStore by preferencesDataStore(name = "session_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ID = intPreferencesKey("user_id")
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { it[IS_LOGGED_IN] ?: false }

    val userIdFlow: Flow<Int?> = context.dataStore.data
        .map { it[USER_ID] }

    suspend fun saveSession(userId: Int) {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = true
            it[USER_ID] = userId
        }
    }

    suspend fun logout() {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = false
        }
    }

    suspend fun clearRememberedUser() {
        context.dataStore.edit {
            it.clear()
        }
    }
}