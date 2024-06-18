package com.example.easemind.data.repository

import com.example.easemind.data.pref.UserPreference

class UserRepository private constructor(

    private val userPreference: UserPreference
) {
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }

}