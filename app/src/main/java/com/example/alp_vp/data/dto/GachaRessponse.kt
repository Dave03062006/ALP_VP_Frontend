package com.example.alp_vp.data.dto

data class GachaResponse(
    val success: Boolean,
    val data: GachaData
)

data class GachaData(
    val results: List<GachaResultDto>,
    val rolls: Int,
    val remainingPoints: Int
)

data class GachaResultDto(
    val itemId: Int,
    val itemName: String
)