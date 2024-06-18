package com.example.easemind.ui.profile.editProfileFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easemind.data.repository.UserRepository
import com.example.easemind.data.response.EditProfileResponse
import com.example.easemind.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _editProfile = MutableLiveData<EditProfileResponse>()
    val editProfile: LiveData<EditProfileResponse> = _editProfile

    suspend fun editProfile(username: String?, age: String?, gender: String?) {
        _isLoading.value = true
        val token = userRepository.getSession().first().token
        val authorization = "Bearer $token"

        val usernameBody = username?.toRequestBody("text/plain".toMediaTypeOrNull())
        val ageBody = age?.toRequestBody("text/plain".toMediaTypeOrNull())
        val genderBody = gender?.toRequestBody("text/plain".toMediaTypeOrNull())

        val client = ApiConfig.getApiService().editProfile(authorization, usernameBody, ageBody, genderBody)
        client.enqueue(object : Callback<EditProfileResponse> {
            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _editProfile.value = response.body()
                    response.body()?.let {
                        viewModelScope.launch {
                            val updatedUser = userRepository.getSession().first().copy(
                                username = username ?: userRepository.getSession().first().username,
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
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    companion object{
        private const val TAG = "EditProfileViewModel"
    }
}