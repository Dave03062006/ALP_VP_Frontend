package com.example.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alp_vp.data.dto.game.GameResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx. coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {

    private val _gamesList = MutableStateFlow<List<GameResponse>>(emptyList())
    val gamesList: StateFlow<List<GameResponse>> = _gamesList.asStateFlow()

    private val _selectedGameId = MutableStateFlow<Int?>(null)
    val selectedGameId: StateFlow<Int?> = _selectedGameId.asStateFlow()

    private val _inputAmount = MutableStateFlow("")
    val inputAmount: StateFlow<String> = _inputAmount.asStateFlow()

    private val _resultIdr = MutableStateFlow(0.0)
    val resultIdr:  StateFlow<Double> = _resultIdr.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _currentExchangeRate = MutableStateFlow(0.0)
    val currentExchangeRate:  StateFlow<Double> = _currentExchangeRate.asStateFlow()

    init {
        fetchGames()
    }

    private fun fetchGames() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // TODO: Replace with actual API call
                // val response = repository.getGames()
                // _gamesList. value = response

                // Mock data - replace with your API call
                _gamesList.value = listOf(
                    GameResponse(1, "Valorant", "VP"),
                    GameResponse(2, "Genshin Impact", "Genesis Crystals"),
                    GameResponse(3, "Mobile Legends", "Diamonds"),
                    GameResponse(4, "PUBG Mobile", "UC")
                )
            } catch (e:  Exception) {
                _errorMessage.value = "Failed to load games:  ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectGame(gameId: Int) {
        _selectedGameId.value = gameId
        _resultIdr.value = 0.0
        _errorMessage.value = null
    }

    fun updateInputAmount(amount:  String) {
        _inputAmount.value = amount
        _errorMessage.value = null
    }

    fun fetchExchangeRate(gameId: Int) {
        viewModelScope.launch {
            _isLoading. value = true
            try {
                // TODO: Replace with actual API call
                // val rate = repository.getExchangeRate(gameId)
                // _currentExchangeRate.value = rate

                // Mock exchange rates - replace with your API call
                _currentExchangeRate. value = when (gameId) {
                    1 -> 165.0  // Valorant VP
                    2 -> 200.0  // Genshin Genesis Crystals
                    3 -> 250.0  // ML Diamonds
                    4 -> 150.0  // PUBG UC
                    else -> 0.0
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch exchange rate:  ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun calculate() {
        val amount = _inputAmount.value. toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _errorMessage. value = "Please enter a valid amount"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // TODO: Replace with actual API call if needed
                // val result = repository.calculateCurrency(selectedGameId.value, amount)
                // _resultIdr. value = result

                // Local calculation
                calculateManual()
            } catch (e: Exception) {
                _errorMessage.value = "Calculation failed: ${e.message}"
            } finally {
                _isLoading. value = false
            }
        }
    }

    fun calculateManual() {
        val amount = _inputAmount.value.toDoubleOrNull() ?: 0.0
        val rate = _currentExchangeRate.value

        if (amount > 0 && rate > 0) {
            _resultIdr.value = amount * rate
            _errorMessage.value = null
        } else if (amount <= 0) {
            _errorMessage. value = "Please enter a valid amount"
        } else {
            _errorMessage. value = "Exchange rate not available"
        }
    }
}