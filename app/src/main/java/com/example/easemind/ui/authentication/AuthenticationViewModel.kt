package com.example.easemind.ui.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.easemind.data.pref.UserModel
import com.example.easemind.data.repository.UserRepository
import com.example.easemind.data.response.AuthenticationResponse
import com.example.easemind.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthenticationViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<AuthenticationResponse>()
    val loginUser: LiveData<AuthenticationResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun getSession() : LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun login(email: String, username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().userAuthentication(email, username)
        client.enqueue(object : Callback<AuthenticationResponse> {
            override fun onResponse(
                call: Call<AuthenticationResponse>,
                response: Response<AuthenticationResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    companion object{
        private const val TAG = "AuthenticationViewModel"
    }
}