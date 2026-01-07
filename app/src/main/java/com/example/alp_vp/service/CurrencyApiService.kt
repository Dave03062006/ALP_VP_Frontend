package com.example.alp_vp. service

import com. example.alp_vp.dto. ConversionResultResponse
import com.example.alp_vp. dto.ConvertCurrencyRequest
import com.example.alp_vp.dto.CurrencyRateResponse
import com.example.alp_vp.dto.GameResponse
import com. example.alp_vp.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CurrencyApiService {

    @GET("/api/games")  // Added /api prefix
    suspend fun getAllGames(): WebResponse<List<GameResponse>>

    @POST("/api/currency/convert")  // Added /api prefix
    suspend fun convertCurrency(@Body request: ConvertCurrencyRequest): WebResponse<ConversionResultResponse>

    @GET("/api/currency-rates")  // Added /api prefix
    suspend fun getCurrencyRates(@Query("gameId") gameId: Int): WebResponse<List<CurrencyRateResponse>>
}