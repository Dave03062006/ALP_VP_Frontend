package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.*

interface EventRepository {
    suspend fun createEvent(req: CreateEventRequest): EventResponse
    suspend fun updateEvent(id: Int, req: UpdateEventRequest): EventResponse
    suspend fun getEvent(id: Int): EventResponse
    suspend fun getEventsByGame(gameId: Int): List<EventResponse>
    suspend fun deleteEvent(id: Int)
}

