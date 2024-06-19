package com.example.easemind.ui.profile.editProfileFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easemind.data.EditProfileRequest
import com.example.easemind.data.repository.UserRepository
import com.example.easemind.data.response.EditProfileResponse
import com.example.easemind.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _editProfile = MutableLiveData<EditProfileResponse>()
    val editProfile: LiveData<EditProfileResponse> = _editProfile

    suspend fun editProfile(userName: String?, age: String?, gender: String?) {
        // _isLoading.value = true
        val token = userRepository.getSession().first().token
        val authorization = "Bearer $token"

        val request = EditProfileRequest(userName, age, gender)
        Log.d(TAG, "Request: userName=$userName, age=$age, gender=$gender")
        val client = ApiConfig.getApiService().editProfile(authorization, request)
        client.enqueue(object : Callback<EditProfileResponse> {
            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>
            ) {
                // _isLoading.value = false
                if (response.isSuccessful) {
                    _editProfile.value = response.body()
                    response.body()?.let {
                        viewModelScope.launch {
                            val updatedUser = userRepository.getSession().first().copy(
                                username = userName ?: userRepository.getSession().first().username,
                                age = age ?: userRepository.getSession().first().age,
                                gender = gender ?: userRepository.getSession().first().gender
                            )
                            userRepository.saveSession(updatedUser)
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                // _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "EditProfileViewModel"
    }
}
