package com.example.easemind.data.repository

import android.util.Log
import com.example.easemind.data.pref.UserModel
import com.example.easemind.data.pref.UserPreference
import com.example.easemind.data.response.JournalsItem
import com.example.easemind.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import com.example.easemind.data.response.UserResponse
import com.example.easemind.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun getJournal(token: String) : List<JournalsItem> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserJournal("Bearer $token")
                response.journals
            } catch (e: Exception) {
                Log.e(TAG, "getStories: ${e.message}", e)
                emptyList()
            }
        }
    }

    suspend fun getUserProfile(token: String): UserResponse {
        return ApiConfig.getApiService().getUser("Bearer $token")
    }

    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(userPreference: UserPreference, apiService: ApiService): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}