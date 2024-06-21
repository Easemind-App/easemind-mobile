package com.example.easemind.di

import android.content.Context
import com.example.easemind.data.pref.UserPreference
import com.example.easemind.data.pref.dataStore
import com.example.easemind.data.repository.UserRepository
import com.example.easemind.data.retrofit.ApiService

object UserInjection {
    fun provideRepository(context: Context, apiService: ApiService): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref, apiService)
    }
}