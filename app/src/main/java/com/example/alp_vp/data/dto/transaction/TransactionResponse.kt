package com.example.alp_vp.data.dto.transaction

data class TransactionResponse(
    val id: Int,
    val profileId: String,
    val gameId: Int,
    val transactionTypeId: Int,
    val amount: Double,
    val eventId: Int?,
    val createdAt: String
)


