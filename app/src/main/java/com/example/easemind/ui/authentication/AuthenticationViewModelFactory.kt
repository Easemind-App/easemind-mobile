package com.example.easemind.ui.authentication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easemind.data.repository.UserRepository
import com.example.easemind.di.UserInjection

class AuthenticationViewModelFactory(private val userRepository: UserRepository) :  ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
            return AuthenticationViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: AuthenticationViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): AuthenticationViewModelFactory {
            if (instance == null) {
                synchronized(AuthenticationViewModelFactory::class.java) {
                    instance = AuthenticationViewModelFactory(UserInjection.provideRepository(context))
                }
            }
            return instance as AuthenticationViewModelFactory
        }
    }
}