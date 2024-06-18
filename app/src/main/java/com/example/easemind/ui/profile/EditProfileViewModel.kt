package com.example.easemind.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easemind.data.repository.UserRepository
import com.example.easemind.data.response.EditProfileResponse
import okhttp3.RequestBody

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _editProfile = MutableLiveData<EditProfileResponse>()
    val editAccount: LiveData<EditProfileResponse> = _editProfile

    suspend fun editProfile(username: RequestBody, age: RequestBody, gender: RequestBody, updatedUsername: String, updatedAge: String, updatedGender: String) {

    }
}