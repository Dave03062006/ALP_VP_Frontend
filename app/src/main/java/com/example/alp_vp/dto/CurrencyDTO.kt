package com.example.alp_vp.model

import com.google.gson.annotations.SerializedName

// --- 1. Model untuk List Game (GET /games) ---
// Cocok dengan model Game di Backend
data class GameResponse(
    val id: Int,
    val name: String,
    val iconUrl: String?, // Backend pakai iconUrl, bukan icon
    val isActive: Boolean
)

// --- 2. Model untuk Request Hitung (POST /currency/convert) ---
// Cocok dengan interface ConvertCurrencyRequest di Backend
data class ConvertCurrencyRequest(
    val gameId: Int,
    val currencyName: String,
    val amount: Int
)

// --- 3. Model untuk Response Hitung ---
// Cocok dengan interface ConversionResult di Backend
data class ConversionResultResponse(
    val gameId: Int,
    val currencyName: String,
    val amount: Int,
    val idrValue: Double // Hasil hitungan (Float/Double di backend)
)

// --- 4. Model untuk Rate (Tabel Harga) ---
// Cocok dengan model CurrencyRate di Backend
data class CurrencyRateResponse(
    val id: Int,
    val gameId: Int,
    val currencyName: String,
    val toIDR: Double // Harga per 1 unit currency
)