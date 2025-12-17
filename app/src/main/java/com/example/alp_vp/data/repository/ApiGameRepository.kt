package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.*
import com.example.alp_vp.data.service.ApiService
import java.io.IOException

class ApiGameRepository(private val service: ApiService) : GameRepository {

    override suspend fun createGame(req: CreateGameRequest): GameResponse {
        val resp = service.createGame(req)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Create game failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun updateGame(id: Int, req: UpdateGameRequest): GameResponse {
        val resp = service.updateGame(id, req)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Update game failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getGames(onlyActive: Boolean?): List<GameResponse> {
        val resp = service.getGames(onlyActive)
        if (resp.isSuccessful) return resp.body() ?: emptyList()
        throw IOException("Get games failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getGame(id: Int): GameResponse {
        val resp = service.getGame(id)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Get game failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun deleteGame(id: Int) {
        val resp = service.deleteGame(id)
        if (!resp.isSuccessful) throw IOException("Delete game failed: ${resp.code()} ${resp.message()}")
    }
}

