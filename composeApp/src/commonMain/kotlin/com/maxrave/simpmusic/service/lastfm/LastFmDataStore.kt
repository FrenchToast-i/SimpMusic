package com.maxrave.simpmusic.service.lastfm

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore for Last.fm credentials
 */
class LastFmDataStore(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        val LAST_FM_SESSION_KEY = stringPreferencesKey("last_fm_session_key")
        val LAST_FM_USER_NAME = stringPreferencesKey("last_fm_user_name")
        val LAST_FM_TOKEN = stringPreferencesKey("last_fm_token")
    }

    val sessionKey: Flow<String?> = dataStore.data.map { preferences ->
        preferences[LAST_FM_SESSION_KEY]
    }

    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[LAST_FM_USER_NAME]
    }

    val token: Flow<String?> = dataStore.data.map { preferences ->
        preferences[LAST_FM_TOKEN]
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        !preferences[LAST_FM_SESSION_KEY].isNullOrEmpty()
    }

    suspend fun saveSession(sessionKey: String, userName: String) {
        dataStore.edit { preferences ->
            preferences[LAST_FM_SESSION_KEY] = sessionKey
            preferences[LAST_FM_USER_NAME] = userName
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[LAST_FM_TOKEN] = token
        }
    }

    suspend fun clearCredentials() {
        dataStore.edit { preferences ->
            preferences.remove(LAST_FM_SESSION_KEY)
            preferences.remove(LAST_FM_USER_NAME)
            preferences.remove(LAST_FM_TOKEN)
        }
    }
}
