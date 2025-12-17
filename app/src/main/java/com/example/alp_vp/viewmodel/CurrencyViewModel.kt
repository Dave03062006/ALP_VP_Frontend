package com.example.alp_vp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alp_vp.dto.ConvertCurrencyRequest
import com.example.alp_vp.dto.GameResponse
import com.example.alp_vp.model.Voucher
import com.example.alp_vp.container.RetrofitClient
import com.example.alp_vp.repository.CurrencyRepository
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {

    private val repository = CurrencyRepository(RetrofitClient.instance)

    var gamesList = mutableStateListOf<GameResponse>()
        private set

    var vouchersList = mutableStateListOf<Voucher>()
        private set

    var inputAmount by mutableStateOf("")
    var selectedGameId by mutableStateOf(0)

    var resultIdr by mutableStateOf(0.0)
    var currentRate by mutableStateOf(0.0)

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadPageData() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = repository.getAllGames()

                val fetchedGames = response.data

                gamesList.clear()
                gamesList.addAll(fetchedGames)

                if (gamesList.isNotEmpty()) {
                    selectedGameId = gamesList[0].id
                    fetchExchangeRate(selectedGameId)
                }

                vouchersList.clear()
                vouchersList.addAll(
                    listOf(
                        Voucher(1, "Cashback 50%", "50%", "https://dummyimage.com/"),
                        Voucher(2, "Diskon 20%", "20%", "https://dummyimage.com/")
                    )
                )

            } catch (e: Exception) {
                errorMessage = "Gagal memuat data: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchExchangeRate(gameId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCurrencyRates(gameId)

                val rates = response.data

                if (rates.isNotEmpty()) {
                    currentRate = rates[0].toIDR
                } else {
                    currentRate = 0.0
                }
            } catch (e: Exception) {
                currentRate = 0.0
            }
        }
    }

    fun calculate() {
        val amount = inputAmount.toIntOrNull()

        if (amount != null && amount > 0 && selectedGameId != 0) {
            viewModelScope.launch {
                isLoading = true
                try {
                    val selectedGame = gamesList.find { it.id == selectedGameId }
                    val currencyName = selectedGame?.currency ?: "Currency"

                    val request = ConvertCurrencyRequest(
                        gameId = selectedGameId,
                        currencyName = currencyName,
                        amount = amount
                    )

                    val response = repository.convertCurrency(request)

                    resultIdr = response.data.idrValue

                } catch (e: Exception) {
                    println("Error calculating: ${e.message}")
                    resultIdr = 0.0
                } finally {
                    isLoading = false
                }
            }
        } else {
            resultIdr = 0.0
        }
    }

    fun calculateManual(amount: String) {
        val valAmount = amount.toIntOrNull() ?: 0
        resultIdr = valAmount * currentRate
    }
}