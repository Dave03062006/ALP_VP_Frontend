package com.example.alp_vp.ui.model

data class GachaResultUi(
    val itemId: Int,
    val itemName: String
)

data class GachaUiState(
    val isLoading: Boolean = false,
    val points: Int = 0,
    val results: List<GachaResultUi> = emptyList(),
    val error: String? = null
)