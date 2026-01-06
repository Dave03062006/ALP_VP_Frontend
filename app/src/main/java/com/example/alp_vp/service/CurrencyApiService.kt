package com.example.alp_vp.service

import com.example.alp_vp.data.dto.GameResponse
import com.example.alp_vp.dto.ConversionResultResponse
import com.example.alp_vp.dto.ConvertCurrencyRequest
import com.example.alp_vp.dto.CurrencyRateResponse
import com.example.alp_vp.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CurrencyApiService {

    @GET("/games")
    suspend fun getAllGames(): WebResponse<List<com.example.alp_vp.dto.GameResponse>>

    @POST("/currency/convert")
    suspend fun convertCurrency(@Body request: ConvertCurrencyRequest): WebResponse<ConversionResultResponse>

    @GET("/currency-rates")
    suspend fun getCurrencyRates(@Query("gameId") gameId: Int): WebResponse<List<CurrencyRateResponse>>
}