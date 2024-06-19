package com.example.easemind.ui.profile.profileFragment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easemind.data.repository.UserRepository
import com.example.easemind.di.UserInjection

class ProfileViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileViewModelFactory? = null

        fun getInstance(context: Context): ProfileViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val repository = UserInjection.provideRepository(context)
                ProfileViewModelFactory(repository).also {
                    INSTANCE = it
                }
            }
        }
    }
}
