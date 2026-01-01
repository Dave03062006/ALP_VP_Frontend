package com.example.alp_vp.data.dto.transaction

data class CreateTransactionRequest(
    val gameId: Int,
    val transactionTypeId: Int,
    val amount: Double,
    val eventId: Int? = null
)
