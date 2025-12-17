package com.example.alp_vp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alp_vp.model.Game
import com.example.alp_vp.model.Voucher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {

    // --- STATE VARIABLES (Data yang berubah-ubah di layar) ---

    // List Game untuk Grid
    var gamesList = mutableStateListOf<Game>()
        private set

    // List Voucher untuk Slider
    var vouchersList = mutableStateListOf<Voucher>()
        private set

    // Input dari User
    var inputAmount by mutableStateOf("") // Menggunakan String agar textfield aman
    var selectedGameId by mutableStateOf(1) // Default ID game pertama

    // Hasil Perhitungan
    var resultIdr by mutableStateOf(0.0)

    // Status Loading (Opsional, untuk efek loading)
    var isLoading by mutableStateOf(false)

    // --- FUNGSI LOGIKA ---

    // 1. Dipanggil saat halaman dibuka (Load Data Dummy/API)
    fun loadPageData() {
        viewModelScope.launch {
            isLoading = true

            // Simulasi delay ambil data dari server
            delay(500)

            // DATA DUMMY (Nanti diganti dengan response API)
            gamesList.clear()
            gamesList.addAll(
                listOf(
                    Game(1, "Mobile Legends", 0, "https://bit.ly/3uH8h2c"),
                    Game(2, "Valorant", 0, "https://bit.ly/3uH8h2c"),
                    Game(3, "Genshin Impact", 0, "https://bit.ly/3uH8h2c"),
                    Game(4, "PUBG Mobile", 0, "https://bit.ly/3uH8h2c"),
                    Game(5, "Free Fire", 0, "https://bit.ly/3uH8h2c"),
                    Game(6, "HSR", 0, "https://bit.ly/3uH8h2c")
                )
            )

            vouchersList.clear()
            vouchersList.addAll(
                listOf(
                    Voucher(1, "Cashback 50%", "50%", "https://dummyimage.com/"),
                    Voucher(2, "Diskon 20%", "20%", "https://dummyimage.com/")
                )
            )

            isLoading = false
        }
    }

    // 2. Dipanggil saat tombol "Calculate" ditekan
    fun calculate() {
        val amount = inputAmount.toIntOrNull()

        if (amount != null && amount > 0) {
            viewModelScope.launch {
                // Di sini nanti panggil API Backend kamu:
                // val response = Repository.calculate(selectedGameId, amount)
                // resultIdr = response.idrValue

                // --- SIMULASI LOGIKA SEMENTARA ---
                // Anggap rate berbeda tiap game
                val rate = when(selectedGameId) {
                    1 -> 300.0 // MLBB: 1 Diamond = Rp 300
                    2 -> 150.0 // Valorant: 1 VP = Rp 150
                    3 -> 250.0 // Genshin: 1 Genesis = Rp 250
                    else -> 100.0
                }

                resultIdr = amount * rate
            }
        } else {
            // Reset jika input invalid
            resultIdr = 0.0
        }
    }
}