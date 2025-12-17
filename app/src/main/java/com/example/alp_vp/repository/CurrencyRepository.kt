package com.example.alp_vp.repository

import com.example.alp_vp.model.ConversionResultResponse
import com.example.alp_vp.model.ConvertCurrencyRequest
import com.example.alp_vp.model.CurrencyRateResponse
import com.example.alp_vp.model.GameResponse
import com.example.alp_vp.service.CurrencyApiService

class CurrencyRepository(private val apiService: CurrencyApiService) {

    suspend fun getGames(): List<GameResponse> {
        return apiService.getAllGames()
    }

    suspend fun calculatePrice(gameId: Int, currencyName: String, amount: Int): Double {
        val request = ConvertCurrencyRequest(
            gameId = gameId,
            currencyName = currencyName,
            amount = amount
        )
        val response = apiService.convertCurrency(request)
        return response.idrValue
    }

    suspend fun getRates(gameId: Int): List<CurrencyRateResponse> {
        return apiService.getCurrencyRates(gameId)
    }
}