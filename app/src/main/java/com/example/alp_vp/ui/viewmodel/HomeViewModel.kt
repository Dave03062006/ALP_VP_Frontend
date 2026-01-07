package com.example.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.alp_vp.VPApplication
import com.example.alp_vp.data.dto.GameResponse
import com.example.alp_vp.data.dto.transaction.CreateTransactionRequest
import com.example.alp_vp.data.dto.transaction.TransactionResponse
import com.example.alp_vp.data.dto.transaction.TransactionStatisticsResponse
import com.example.alp_vp.data.local.SessionManager
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
    val allTransactions: List<TransactionResponse> = emptyList(), // Store all transactions for ranking
    val statistics: TransactionStatisticsResponse? = null,
    val gameSpendingRanking: List<GameSpending> = emptyList(), // Add ranking
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class GameSpending(
    val gameId: Int,
    val gameName: String,
    val totalSpent: Double,
    val transactionCount: Int
)

class HomeViewModel(
    private val transactionRepository: TransactionRepository,
    private val gameRepository: GameRepository,
    private val profileRepository: ProfileRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val currentProfileId: Int
        get() = sessionManager.getUserId()

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

                // Load ALL transactions (without limit) for accurate ranking
                println("DEBUG: Fetching all transactions...")
                val allTransactions = transactionRepository.getTransactions(
                    profileId = currentProfileId
                )
                println("DEBUG: All transactions loaded - Count: ${allTransactions.size}")

                // Calculate game spending ranking
                val gameSpendingRanking = calculateGameSpendingRanking(allTransactions)
                println("DEBUG: Game spending ranking calculated - ${gameSpendingRanking.size} games")

                // Apply filter based on selected game
                val filteredTransactions = filterTransactions(allTransactions, _uiState.value.selectedGame?.id)
                val filteredStatistics = calculateStatistics(filteredTransactions)

                _uiState.value = _uiState.value.copy(
                    userPoints = profile.points,
                    games = games,
                    transactions = filteredTransactions.take(10), // Show recent 10
                    allTransactions = allTransactions,
                    statistics = filteredStatistics,
                    gameSpendingRanking = gameSpendingRanking,
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

    private fun filterTransactions(transactions: List<TransactionResponse>, gameId: Int?): List<TransactionResponse> {
        return if (gameId != null) {
            transactions.filter { it.gameId == gameId }
        } else {
            transactions
        }
    }

    private fun calculateStatistics(transactions: List<TransactionResponse>): TransactionStatisticsResponse {
        val totalSpent = transactions
            .filter {
                // Consider it a purchase if:
                // 1. Amount is positive (user spent money)
                // 2. OR transaction type contains "purchase", "bundle", or "discount"
                it.amount > 0 ||
                it.transactionType?.typeName?.lowercase()?.let { typeName ->
                    typeName.contains("purchase") ||
                    typeName.contains("bundle") ||
                    typeName.contains("discount")
                } == true
            }
            .sumOf { kotlin.math.abs(it.amount) }

        val totalEarned = transactions
            .sumOf { it.pointsEarned.toDouble() }

        return TransactionStatisticsResponse(
            totalSpent = totalSpent,
            totalEarned = totalEarned,
            transactionsCount = transactions.size
        )
    }

    private fun calculateGameSpendingRanking(transactions: List<TransactionResponse>): List<GameSpending> {
        // Group transactions by game
        val gameGroups = transactions.groupBy { it.gameId }

        // Calculate spending per game
        val gameSpendingList = gameGroups.map { (gameId, gameTransactions) ->
            val gameName = gameTransactions.firstOrNull()?.game?.name ?: "Unknown Game"
            val totalSpent = gameTransactions
                .filter {
                    // Use the same logic as calculateStatistics
                    it.amount > 0 ||
                    it.transactionType?.typeName?.lowercase()?.let { typeName ->
                        typeName.contains("purchase") ||
                        typeName.contains("bundle") ||
                        typeName.contains("discount")
                    } == true
                }
                .sumOf { kotlin.math.abs(it.amount) }

            GameSpending(
                gameId = gameId,
                gameName = gameName,
                totalSpent = totalSpent,
                transactionCount = gameTransactions.size
            )
        }

        // Sort by total spent (descending) and return
        return gameSpendingList.sortedByDescending { it.totalSpent }
    }

    fun createTransaction(request: CreateTransactionRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                println("DEBUG: Creating transaction...")
                val createdTransaction = transactionRepository.createTransaction(currentProfileId, request)
                println("DEBUG: Transaction created successfully: ${createdTransaction.id}")

                // Reload all data to ensure everything is fresh
                loadData()

                println("DEBUG: Data reloaded after transaction creation")
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
        println("DEBUG: Selecting game: ${game?.name ?: "All Games"}")
        _uiState.value = _uiState.value.copy(selectedGame = game)

        // Re-filter transactions and recalculate statistics
        val filteredTransactions = filterTransactions(_uiState.value.allTransactions, game?.id)
        val filteredStatistics = calculateStatistics(filteredTransactions)

        println("DEBUG: Filtered ${filteredTransactions.size} transactions")
        println("DEBUG: Recalculated total spent: ${filteredStatistics.totalSpent}")

        _uiState.value = _uiState.value.copy(
            transactions = filteredTransactions.take(10),
            statistics = filteredStatistics
        )

        println("DEBUG: State updated with new statistics")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VPApplication)
                HomeViewModel(
                    transactionRepository = application.container.transactionRepository,
                    gameRepository = application.container.gameRepository,
                    profileRepository = application.container.profileRepository,
                    sessionManager = application.container.sessionManager
                )
            }
        }
    }
}
