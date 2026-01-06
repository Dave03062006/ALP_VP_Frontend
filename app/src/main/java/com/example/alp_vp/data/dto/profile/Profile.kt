package com.example.alp_vp.data.dto

data class Profile(
    val id: String,
    val username: String,
    val email: String,
    val displayName: String?,
    val points: Int = 0,
    val avatarUrl: String? = null,
    val createdAt: String? = null
)

