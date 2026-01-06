package com.example.alp_vp.data.dto

data class UpdateEventRequest(
    val eventName: String? = null,
    val startsAt: String? = null,
    val endsAt: String? = null
)