package com.example.alp_vp.data.dto

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val displayName: String? = null
)
