package com.example.alp_vp.model

data class ConvertCurrencyRequest(
    val gameId: Int,
    val currencyName: String,
    val amount: Int
)

data class WebResponse<T>(
    val data: T,
    val success: Boolean? = true
)

data class GameResponse(
    val id: Int,
    val name: String,
    val iconUrl: String?,
    val currency: String? = "Currency",
    val isActive: Boolean? = true
)

data class ConversionResult(
    val gameId: Int,
    val currencyName: String,
    val amount: Int,
    val idrValue: Double
)