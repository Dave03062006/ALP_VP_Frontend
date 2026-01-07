package com.example.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.alp_vp.VPApplication
import com.example.alp_vp.data.local.SessionManager
import com.example.alp_vp.data.repository.GachaRepository
import com.example.alp_vp.ui.model.GachaResultUi
import com.example.alp_vp.ui.model.GachaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GachaViewModel(
    private val repository: GachaRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // ✅ GAME YANG DIPILIH
    private val _selectedGameId = MutableStateFlow(1)
    val selectedGameId: StateFlow<Int> = _selectedGameId

    fun selectGame(gameId: Int) {
        _selectedGameId.value = gameId
    }

    // ✅ UI STATE
    private val _uiState = MutableStateFlow(GachaUiState())
    val uiState: StateFlow<GachaUiState> = _uiState

    private val currentProfileId: Int
        get() = sessionManager.getUserId()

    private val gameId = 1

    fun singlePull() {
        rollGacha(currentProfileId, gameId, rolls = 1)
    }

    fun tenPull() {
        rollGacha(currentProfileId, gameId, rolls = 10)
    }

    private fun rollGacha(profileId: Int, gameId: Int, rolls: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val response = repository.performGacha(profileId, gameId, rolls)

                val uiResults = response.data.results.map {
                    GachaResultUi(
                        itemId = it.itemId,
                        itemName = it.itemName,
                        imageUrl = it.imageUrl
                    )
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    points = response.data.remainingPoints,
                    results = uiResults,
                    error = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VPApplication)
                GachaViewModel(
                    repository = application.container.gachaRepository,
                    sessionManager = application.container.sessionManager
                )
            }
        }
    }
}

