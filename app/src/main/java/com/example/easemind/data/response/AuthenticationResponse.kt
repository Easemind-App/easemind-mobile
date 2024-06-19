package com.example.easemind.data.response

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("token")
    val token: String
)

data class UserResponse(

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("userName")
    val userName: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("age")
    val age: String
)