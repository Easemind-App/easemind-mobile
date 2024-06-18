package com.example.easemind.di

import android.content.Context
import com.example.easemind.data.pref.UserPreference
import com.example.easemind.data.pref.dataStore
import com.example.easemind.data.repository.UserRepository

object UserInjection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}