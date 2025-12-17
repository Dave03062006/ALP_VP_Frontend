package com.example.alp_vp.service

import com.example.alp_vp.model.ConversionResultResponse
import com.example.alp_vp.model.ConvertCurrencyRequest
import com.example.alp_vp.model.CurrencyRateResponse
import com.example.alp_vp.model.GameResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CurrencyApiService {

    @GET("/games")
    suspend fun getAllGames(): List<GameResponse>

    @POST("/currency/convert")
    suspend fun convertCurrency(@Body request: ConvertCurrencyRequest): ConversionResultResponse

    @GET("/currency-rates")
    suspend fun getCurrencyRates(@Query("gameId") gameId: Int): List<CurrencyRateResponse>
}