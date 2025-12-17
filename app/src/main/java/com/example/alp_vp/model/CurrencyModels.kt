package com.example.alp_vp.model

// 1. Request (Apa yang dikirim HP ke Server)
data class ConvertCurrencyRequest(
    val gameId: Int,
    val currencyName: String,
    val amount: Int
)

// 2. Wrapper Response (Bungkus luar JSON)
// Gunakan ini jika backend return: { "data": { ... } }
data class WebResponse<T>(
    val data: T,
    val success: Boolean? = true // Default true atau nullable biar aman
)

// 3. Data Asli (Isi dalamnya)
data class ConversionResult(
    val gameId: Int,
    val currencyName: String,
    val amount: Int,
    val idrValue: Double
)