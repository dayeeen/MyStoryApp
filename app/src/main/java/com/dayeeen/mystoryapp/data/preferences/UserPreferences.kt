package com.dayeeen.mystoryapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dayeeen.mystoryapp.data.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserData) {
        dataStore.edit { preferences ->
            preferences[IS_LOGIN_KEY] = true
            preferences[TOKEN_KEY] = user.token
            preferences[EMAIL_KEY] = user.email
        }
    }

    fun getSession(): Flow<UserData> {
        return dataStore.data.map { preferences ->
            UserData(
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}