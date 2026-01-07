package com.example.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.alp_vp.VPApplication
import com.example.alp_vp.data.dto.GameResponse
import com.example.alp_vp.data.dto.transaction.TransactionResponse
import com.example.alp_vp.data.local.SessionManager
import com.example.alp_vp.data.repository.GameRepository
import com.example.alp_vp.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistoryUiState(
    val transactions: List<TransactionResponse> = emptyList(),
    val filteredTransactions: List<TransactionResponse> = emptyList(),
    val games: List<GameResponse> = emptyList(),
    val selectedGameId: Int? = null, // null means "All Games"
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val totalSpent: Double = 0.0,
    val totalEarned: Double = 0.0
)

class HistoryViewModel(
    private val transactionRepository: TransactionRepository,
    private val gameRepository: GameRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val currentProfileId: Int
        get() = sessionManager.getUserId()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                // Load games for filter
                val games = gameRepository.getGames(onlyActive = true)

                // Load all transactions
                val transactions = transactionRepository.getTransactions(
                    profileId = currentProfileId
                )

                // Calculate totals
                val totalSpent = transactions
                    .filter {
                        // Use consistent logic: amount > 0 means user spent money
                        it.amount > 0 ||
                        it.transactionType?.typeName?.lowercase()?.let { typeName ->
                            typeName.contains("purchase") ||
                            typeName.contains("bundle") ||
                            typeName.contains("discount")
                        } == true
                    }
                    .sumOf { kotlin.math.abs(it.amount) }

                val totalEarned = transactions
                    .filter { it.transactionType?.typeName?.lowercase()?.contains("reward") == true ||
                              it.transactionType?.typeName?.lowercase()?.contains("refund") == true }
                    .sumOf { it.amount }

                _uiState.value = _uiState.value.copy(
                    transactions = transactions,
                    filteredTransactions = transactions,
                    games = games,
                    isLoading = false,
                    totalSpent = totalSpent,
                    totalEarned = totalEarned
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load transactions: ${e.message}"
                )
            }
        }
    }

    fun filterByGame(gameId: Int?) {
        val filtered = if (gameId == null) {
            _uiState.value.transactions
        } else {
            _uiState.value.transactions.filter { it.gameId == gameId }
        }

        // Recalculate totals for filtered data with consistent logic
        val totalSpent = filtered
            .filter {
                it.amount > 0 ||
                it.transactionType?.typeName?.lowercase()?.let { typeName ->
                    typeName.contains("purchase") ||
                    typeName.contains("bundle") ||
                    typeName.contains("discount")
                } == true
            }
            .sumOf { kotlin.math.abs(it.amount) }

        val totalEarned = filtered
            .filter { it.transactionType?.typeName?.lowercase()?.contains("reward") == true ||
                      it.transactionType?.typeName?.lowercase()?.contains("refund") == true }
            .sumOf { it.amount }

        _uiState.value = _uiState.value.copy(
            selectedGameId = gameId,
            filteredTransactions = filtered,
            totalSpent = totalSpent,
            totalEarned = totalEarned
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VPApplication)
                HistoryViewModel(
                    transactionRepository = application.container.transactionRepository,
                    gameRepository = application.container.gameRepository,
                    sessionManager = application.container.sessionManager
                )
            }
        }
    }
}
