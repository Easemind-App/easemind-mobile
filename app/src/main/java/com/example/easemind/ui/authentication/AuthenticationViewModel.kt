package com.example.easemind.ui.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.easemind.data.request.AuthenticationRequest
import com.example.easemind.data.pref.UserModel
import com.example.easemind.data.repository.UserRepository
import com.example.easemind.data.request.JournalData
import com.example.easemind.data.request.JournalRequest
import com.example.easemind.data.response.AddJournalResponse
import com.example.easemind.data.response.AuthenticationResponse
import com.example.easemind.data.response.JournalResponse
import com.example.easemind.data.response.JournalsItem
import com.example.easemind.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthenticationViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<AuthenticationResponse>()
    val loginUser: LiveData<AuthenticationResponse> = _user

    private val _journal = MutableLiveData<List<JournalsItem>>()
    val journal: LiveData<List<JournalsItem>> = _journal

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _addJournalResult = MutableLiveData<AddJournalResponse>()
    val addJournalResult: LiveData<AddJournalResponse> = _addJournalResult

    private val _journalCheckpoint = MutableLiveData<Boolean>()
    val journalCheckpoint: LiveData<Boolean> = _journalCheckpoint

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
        val request = AuthenticationRequest(email, username)
        val client = ApiConfig.getApiService().userAuthentication(request)
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

    fun getJournal(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _journal.value = userRepository.getJournal(token)
            _isLoading.value = false
        }
    }

    fun getJournalCheckpoint(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _journalCheckpoint.value = userRepository.getJournalCheckpoint(token)
            _isLoading.value = false
        }
    }

    fun getSortJournal(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val journals = userRepository.getJournal(token)
                _journal.value = journals.takeLast(7) // Mengambil 7 jurnal terakhir
            } catch (e: Exception) {
                Log.e(TAG, "getJournal: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addJournal(journalDate: String, faceDetection: String, thoughts: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val token = userRepository.getSession().first().token
            Log.d("token", token)
            val authorization = "Bearer $token"
            try {
                val journalData = JournalData(journalDate, faceDetection, thoughts)

                // If user can addJournal then no journal has been added in the same data
                val request = JournalRequest(true, journalData)
                val response = ApiConfig.getApiService().addJournal(token, request)
                _addJournalResult.value = response
            } catch (e: Exception) {
                Log.e(TAG, "addJournal: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    companion object{
        private const val TAG = "AuthenticationViewModel"
    }
}