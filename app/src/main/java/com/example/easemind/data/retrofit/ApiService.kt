package com.example.easemind.data.retrofit

import com.example.easemind.data.response.AuthenticationResponse
import com.example.easemind.data.response.JournalResponse
import com.example.easemind.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth")
    fun userAuthentication (
        @Field("email") email: String,
        @Field("userName") username: String
    ) : Call<AuthenticationResponse>

    @GET("profile")
    suspend fun getUser (
        @Header("Authorization") token: String
    ) : UserResponse

    @GET("journals")
    suspend fun getUserJournal (
        @Header("Authorization") token: String
    ) : JournalResponse
}