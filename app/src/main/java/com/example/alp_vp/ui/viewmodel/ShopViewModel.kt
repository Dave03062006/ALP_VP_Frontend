package com.example.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.alp_vp.VPApplication
import com.example.alp_vp.data.dto.*
import com.example.alp_vp.data.dto.voucher.Voucher
import com.example.alp_vp.data.dto.voucher.VoucherPurchaseRequest
import com.example.alp_vp.data.repository.VoucherRepository
import com.example.alp_vp.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.alp_vp.data.dto.game.GameResponse

data class ShopUiState(
    val vouchers: List<Voucher> = emptyList(),
    val games: List<GameResponse> = emptyList(),
    val selectedGameId: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val purchaseSuccess: Boolean = false
)

class ShopViewModel(
    private val voucherRepository: VoucherRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    private val currentProfileId = "1" // Hardcoded for now

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val games = gameRepository.getGames(onlyActive = true)
                _uiState.value = _uiState.value.copy(
                    games = games,
                    isLoading = false
                )
                // Load vouchers for first game if available
                if (games.isNotEmpty()) {
                    selectGame(games.first().id)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load games"
                )
            }
        }
    }

    fun selectGame(gameId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                selectedGameId = gameId,
                isLoading = true,
                errorMessage = null
            )
            try {
                val vouchers = voucherRepository.getVouchersByGame(gameId)
                _uiState.value = _uiState.value.copy(
                    vouchers = vouchers,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load vouchers"
                )
            }
        }
    }

    fun purchaseVoucher(voucherId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                voucherRepository.purchaseVoucher(
                    currentProfileId,
                    VoucherPurchaseRequest(voucherId)
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    purchaseSuccess = true
                )
                // Reload vouchers
                _uiState.value.selectedGameId?.let { selectGame(it) }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to purchase voucher"
                )
            }
        }
    }

    fun resetPurchaseSuccess() {
        _uiState.value = _uiState.value.copy(purchaseSuccess = false)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VPApplication)
                ShopViewModel(
                    voucherRepository = application.container.voucherRepository,
                    gameRepository = application.container.gameRepository
                )
            }
        }
    }
}

