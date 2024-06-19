package com.example.easemind.data.retrofit

import com.example.easemind.data.AuthenticationRequest
import com.example.easemind.data.response.AuthenticationResponse
import com.example.easemind.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth")
    fun userAuthentication (
        @Body request: AuthenticationRequest
    ) : Call<AuthenticationResponse>

    @GET("profile")
    suspend fun getUser (
        @Header("Authorization") token: String
    ) : UserResponse
}