package com.example.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.alp_vp.VPApplication
import com.example.alp_vp.data.dto.*
import com.example.alp_vp.data.dto.transaction.CreateTransactionRequest
import com.example.alp_vp.data.dto.transaction.TransactionResponse
import com.example.alp_vp.data.dto.transaction.TransactionStatisticsResponse
import com.example.alp_vp.data.repository.GameRepository
import com.example.alp_vp.data.repository.TransactionRepository
import com.example.alp_vp.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val userPoints: Int = 0,
    val selectedGame: GameResponse? = null,
    val games: List<GameResponse> = emptyList(),
    val transactions: List<TransactionResponse> = emptyList(),
    val statistics: TransactionStatisticsResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val transactionRepository: TransactionRepository,
    private val gameRepository: GameRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val currentProfileId: Int = 1

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                println("DEBUG: Starting to load data for profile $currentProfileId")

                // Load profile to get user points
                println("DEBUG: Fetching profile...")
                val profile = profileRepository.getProfile(currentProfileId)
                println("DEBUG: Profile loaded - Points: ${profile.points}")

                // Load games
                println("DEBUG: Fetching games...")
                val games = gameRepository.getGames(onlyActive = true)
                println("DEBUG: Games loaded - Count: ${games.size}")

                // Load transactions
                println("DEBUG: Fetching transactions...")
                val transactions = transactionRepository.getTransactions(
                    profileId = currentProfileId,
                    limit = 10
                )
                println("DEBUG: Transactions loaded - Count: ${transactions.size}")

                // Load statistics
                println("DEBUG: Fetching statistics...")
                val statistics = transactionRepository.getTransactionStatistics(currentProfileId)
                println("DEBUG: Statistics loaded - Total Spent: ${statistics.totalSpent}")

                _uiState.value = _uiState.value.copy(
                    userPoints = profile.points,
                    games = games,
                    transactions = transactions,
                    statistics = statistics,
                    isLoading = false
                )
                println("DEBUG: All data loaded successfully!")
            } catch (e: Exception) {
                println("ERROR: Failed to load data - ${e.message}")
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Connection failed: ${e.message}\n\nIs your backend running on http://localhost:3000?"
                )
            }
        }
    }

    fun createTransaction(request: CreateTransactionRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                println("DEBUG: Creating transaction...")
                val createdTransaction = transactionRepository.createTransaction(currentProfileId, request)
                println("DEBUG: Transaction created successfully: ${createdTransaction.id}")

                // Reload data after creating transaction
                loadData()
            } catch (e: Exception) {
                println("ERROR: Failed to create transaction - ${e.message}")
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to create transaction: ${e.message}"
                )
            }
        }
    }

    fun selectGame(game: GameResponse?) {
        _uiState.value = _uiState.value.copy(selectedGame = game)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VPApplication)
                HomeViewModel(
                    transactionRepository = application.container.transactionRepository,
                    gameRepository = application.container.gameRepository,
                    profileRepository = application.container.profileRepository
                )
            }
        }
    }
}
