package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.*
import com.example.alp_vp.data.service.ApiService
import java.io.IOException

class ApiEventRepository(private val service: ApiService) : EventRepository {

    override suspend fun createEvent(req: CreateEventRequest): EventResponse {
        val resp = service.createEvent(req)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Create event failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun updateEvent(id: Int, req: UpdateEventRequest): EventResponse {
        val resp = service.updateEvent(id, req)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Update event failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getEvent(id: Int): EventResponse {
        val resp = service.getEvent(id)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Get event failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getEventsByGame(gameId: Int): List<EventResponse> {
        val resp = service.getEventsByGame(gameId)
        if (resp.isSuccessful) return resp.body() ?: emptyList()
        throw IOException("Get events by game failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun deleteEvent(id: Int) {
        val resp = service.deleteEvent(id)
        if (!resp.isSuccessful) throw IOException("Delete event failed: ${resp.code()} ${resp.message()}")
    }
}

