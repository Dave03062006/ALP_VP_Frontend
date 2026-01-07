package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.game.GameResponse
import com.example.alp_vp.data.dto.CreateGameRequest
import com. example.alp_vp. data.dto.UpdateGameRequest

interface GameRepository {
    suspend fun createGame(req: CreateGameRequest): GameResponse
    suspend fun updateGame(id: Int, req: UpdateGameRequest): GameResponse
    suspend fun getGames(onlyActive: Boolean?  = null): List<GameResponse>
    suspend fun getGame(id: Int): GameResponse
    suspend fun deleteGame(id: Int)
}