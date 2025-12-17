package com.example.alp_vp.repository

import com.example.alp_vp.dto.ConvertCurrencyRequest
import com.example.alp_vp.dto.ConversionResultResponse
import com.example.alp_vp.dto.CurrencyRateResponse
import com.example.alp_vp.dto.GameResponse
import com.example.alp_vp.model.WebResponse
import com.example.alp_vp.service.CurrencyApiService

class CurrencyRepository(private val apiService: CurrencyApiService) {

    // Hanya memanggil API, return masih berupa WebResponse
    suspend fun getAllGames(): WebResponse<List<GameResponse>> {
        return apiService.getAllGames()
    }

    // Menerima object Request dari ViewModel, bukan parameter satu-satu
    suspend fun convertCurrency(request: ConvertCurrencyRequest): WebResponse<ConversionResultResponse> {
        return apiService.convertCurrency(request)
    }

    // Hanya memanggil API
    suspend fun getCurrencyRates(gameId: Int): WebResponse<List<CurrencyRateResponse>> {
        return apiService.getCurrencyRates(gameId)
    }
}