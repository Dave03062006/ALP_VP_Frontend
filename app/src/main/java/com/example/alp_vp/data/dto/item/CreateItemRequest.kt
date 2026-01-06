package com.example.alp_vp.data.dto.item

data class CreateItemRequest(
    val itemName: String,
    val rarity: String,
    val imageUrl: String?,
    val isMilestone: Boolean = false
)