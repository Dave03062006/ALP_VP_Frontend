package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.GachaRequest
import com.example.alp_vp.data.dto.GachaResponse
import com.example.alp_vp.data.service.GachaService

class GachaRepository(
    private val gachaService: GachaService
) {

    suspend fun performGacha(
        profileId: Int,
        gameId: Int,
        rolls: Int
    ): GachaResponse {
        val request = GachaRequest(
            profileId = profileId,
            gameId = gameId,
            rolls = rolls
        )

        return gachaService.performGacha(request)
    }
}