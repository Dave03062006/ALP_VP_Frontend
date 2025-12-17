package com.example.alp_vp.data.dto.transaction

data class TransactionStatisticsResponse(
    val totalSpent: Double,
    val totalEarned: Double,
    val transactionsCount: Int
)