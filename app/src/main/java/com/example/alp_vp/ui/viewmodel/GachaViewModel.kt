package com.example.alp_vp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alp_vp.data.repository.GachaRepository
import com.example.alp_vp.ui.model.GachaResultUi
import com.example.alp_vp.ui.model.GachaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GachaViewModel(
    private val repository: GachaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GachaUiState())
    val uiState: StateFlow<GachaUiState> = _uiState

    private val profileId = 1
    private val gameId = 1

    fun singlePull() {
        rollGacha(profileId, gameId, rolls = 1)
    }

    fun tenPull() {
        rollGacha(profileId, gameId, rolls = 10)
    }

    private fun rollGacha(profileId: Int, gameId: Int, rolls: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val response = repository.performGacha(profileId, gameId, rolls)

                val uiResults = response.data.results.map {
                    GachaResultUi(
                        itemId = it.itemId,
                        itemName = it.itemName
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
}

