package com.example.easemind.data.retrofit

import com.example.easemind.data.response.AuthenticationResponse
import com.example.easemind.data.response.UserResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {
    @POST("auth")
    fun userAuthentication (
        @Field("email") email: String,
        @Field("userName") username: String
    ) : Call<AuthenticationResponse>

    @GET("profile")
    suspend fun getUser (
//        @Header("Authorization") token: String
    ) : UserResponse


    @PUT("profile/edit")
    suspend fun editProfile(
//        @Header("Authorization") token: String
        @Part("username") username: RequestBody,
        @Part("age") age: RequestBody,
        @Part("gender") gender: RequestBody,
    )

}