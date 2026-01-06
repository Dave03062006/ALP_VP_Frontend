package com.example.alp_vp.data.dto

data class EventResponse(
    val id: Int,
    val gameId: Int,
    val eventName: String,
    val startsAt: String?,
    val endsAt: String?
)
