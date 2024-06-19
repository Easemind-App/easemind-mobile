package com.example.easemind.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[USERNAME] = user.username
            preferences[TOKEN_KEY] = user.token
            preferences[PROFILE_PIC] = user.profilePicture
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                email = preferences[EMAIL_KEY] ?: "",
                username = preferences[USERNAME] ?: "",
                token = preferences[TOKEN_KEY] ?: "",
                profilePicture = preferences[PROFILE_PIC] ?: "",
                age = preferences[AGE_KEY] ?: "",
                gender = preferences[GENDER_KEY] ?: "",
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USERNAME = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val AGE_KEY = stringPreferencesKey("age")
        private val GENDER_KEY = stringPreferencesKey("gender")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val PROFILE_PIC = stringPreferencesKey("profilePicture")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}