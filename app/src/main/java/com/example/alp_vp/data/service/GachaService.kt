package com.example.alp_vp.data.service

import com.example.alp_vp.data.dto.GachaRequest
import com.example.alp_vp.data.dto.GachaResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GachaService {

    @POST("api/gacha")
    suspend fun performGacha(
        @Body request: GachaRequest
    ): GachaResponse
}