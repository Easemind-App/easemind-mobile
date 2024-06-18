package com.example.easemind.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easemind.data.repository.UserRepository
import com.example.easemind.di.UserInjection

class EditProfileViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: EditProfileViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): EditProfileViewModelFactory {
            if (INSTANCE == null) {
                synchronized(EditProfileViewModelFactory::class.java) {
                    INSTANCE = EditProfileViewModelFactory(UserInjection.provideRepository(context))
                }
            }
            return INSTANCE as EditProfileViewModelFactory
        }
    }
}