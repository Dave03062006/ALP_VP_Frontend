package com.example.alp_vp.data.dto

data class CreateEventRequest(
    val gameId: Int,
    val eventName: String,
    val startsAt: String? = null,
    val endsAt: String? = null
)