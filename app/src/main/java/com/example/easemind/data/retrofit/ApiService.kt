package com.example.easemind.data.retrofit

import com.example.easemind.data.request.AuthenticationRequest
import com.example.easemind.data.request.EditProfileRequest
import com.example.easemind.data.request.JournalRequest
import com.example.easemind.data.response.AddJournalResponse
import com.example.easemind.data.response.AuthenticationResponse
import com.example.easemind.data.response.JournalResponse
import com.example.easemind.data.response.EditProfileResponse
import com.example.easemind.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("auth")
    fun userAuthentication (
        @Body request: AuthenticationRequest
    ) : Call<AuthenticationResponse>

    @GET("profile")
    suspend fun getUser (
        @Header("Authorization") token: String
    ) : UserResponse

    @PUT("profile/edit")
    fun editProfile(
        @Header("Authorization") token: String,
        @Body request: EditProfileRequest
    ) : Call<EditProfileResponse>

    @GET("journals")
    suspend fun getUserJournal (
        @Header("Authorization") token: String
    ) : JournalResponse

    @POST("journals/create")
    suspend fun addJournal (
        @Header("Authorization") token: String,
        @Body request: JournalRequest
    ) : AddJournalResponse

}