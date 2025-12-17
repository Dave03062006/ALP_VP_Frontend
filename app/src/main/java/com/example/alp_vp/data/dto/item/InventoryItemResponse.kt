package com.example.alp_vp.data.dto.item

data class InventoryItemResponse(
    val id: Int,
    val itemName: String,
    val imageUrl: String?,
    val rarity: String,
    val acquiredAt: String
)