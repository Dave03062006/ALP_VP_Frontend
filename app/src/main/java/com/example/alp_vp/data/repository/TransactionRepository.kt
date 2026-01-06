package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.transaction.CreateTransactionRequest
import com.example.alp_vp.data.dto.transaction.TransactionResponse
import com.example.alp_vp.data.dto.transaction.TransactionStatisticsResponse

interface TransactionRepository {
    suspend fun createTransaction(profileId: Int, req: CreateTransactionRequest): TransactionResponse
    suspend fun getTransactions(
        profileId: Int,
        gameId: Int? = null,
        startDate: String? = null,
        endDate: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ): List<TransactionResponse>
    suspend fun getTransactionStatistics(profileId: Int): TransactionStatisticsResponse
}

