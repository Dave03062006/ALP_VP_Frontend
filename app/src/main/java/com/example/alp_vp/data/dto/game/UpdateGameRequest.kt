package com.example.alp_vp.data.dto

data class UpdateGameRequest(
    val name: String? = null,
    val iconUrl: String? = null,
    val isActive: Boolean? = null
)