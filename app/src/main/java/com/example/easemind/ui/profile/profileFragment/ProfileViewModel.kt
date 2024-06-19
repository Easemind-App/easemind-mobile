package com.example.easemind.ui.profile.profileFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easemind.data.repository.UserRepository
import com.example.easemind.data.response.UserResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<UserResponse>()
    val userProfile: LiveData<UserResponse> = _userProfile

    fun getUserProfile() {
        viewModelScope.launch {
            val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJhZDlZMWZXbWtZRGZPdkVCa25YZyIsImlhdCI6MTcxODQ1ODI1NSwiZXhwIjoxNzE5MzIyMjU1fQ.Z059aEUb6AzEQJlXQ9agYyJP3l66A3Xc6-HaxHzmKm4"
            val authorization = "Bearer $token"
            try {
                val userResponse = userRepository.getUserProfile(authorization)
                _userProfile.value = userResponse
            } catch (e: Exception) {
            }
        }
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}