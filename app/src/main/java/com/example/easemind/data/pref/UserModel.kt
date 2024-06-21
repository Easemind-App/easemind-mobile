package com.example.easemind.data.pref

data class UserModel(
    val email: String,
    val username: String,
    val age: String?,
    val gender: String?,
    val token: String,
    val isLogin: Boolean = false,
    val profilePicture: String
)