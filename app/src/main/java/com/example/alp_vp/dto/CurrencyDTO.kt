package com.example.alp_vp.model

import com.google.gson.annotations.SerializedName

data class GameResponse(
    val id: Int,
    val name: String,
    val iconUrl: String?,
    val isActive: Boolean
)

data class ConvertCurrencyRequest(
    val gameId: Int,
    val currencyName: String,
    val amount: Int
)

data class ConversionResultResponse(
    val gameId: Int,
    val currencyName: String,
    val amount: Int,
    val idrValue: Double
)

data class CurrencyRateResponse(
    val id: Int,
    val gameId: Int,
    val currencyName: String,
    val toIDR: Double
)