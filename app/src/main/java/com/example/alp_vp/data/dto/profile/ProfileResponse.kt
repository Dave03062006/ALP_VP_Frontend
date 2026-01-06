package com.example.alp_vp.data.dto

data class ProfileResponse(
    val id: String,
    val username: String,
    val email: String,
    val displayName: String?,
    val avatarUrl: String? = null,
    val points: Int = 0,
    val createdAt: String
)
