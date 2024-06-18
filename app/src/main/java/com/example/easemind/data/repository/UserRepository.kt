package com.example.easemind.data.repository
import com.example.easemind.data.pref.UserModel
import com.example.easemind.data.pref.UserPreference
import com.example.easemind.data.response.UserResponse
import com.example.easemind.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun getUserProfile(token: String): UserResponse {
        return ApiConfig.getApiService().getUser("Bearer $token")
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(userPreference: UserPreference): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}