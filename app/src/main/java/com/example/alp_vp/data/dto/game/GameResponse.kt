package com.example.alp_vp.data.dto

data class GameResponse(
    val id: Int,
    val name: String,
    val iconUrl: String?,
    val isActive: Boolean,
    val createdAt: String
)
