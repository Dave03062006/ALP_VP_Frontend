// kotlin
package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.transaction.CreateTransactionRequest
import com.example.alp_vp.data.dto.transaction.TransactionResponse
import com.example.alp_vp.data.dto.transaction.TransactionStatisticsResponse
import com.example.alp_vp.data.service.ApiService
import java.io.IOException

class ApiTransactionRepository(private val service: ApiService) : TransactionRepository {

    override suspend fun createTransaction(profileId: Int, req: CreateTransactionRequest): TransactionResponse {
        println("DEBUG: Creating transaction for profile $profileId")
        val resp = service.createTransaction(profileId, req)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Create transaction failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getTransactions(
        profileId: Int,
        gameId: Int?,
        startDate: String?,
        endDate: String?,
        limit: Int?,
        offset: Int?
    ): List<TransactionResponse> {
        val resp = service.getTransactions(profileId, gameId, startDate, endDate, limit, offset)
        if (resp.isSuccessful) return resp.body() ?: emptyList()
        throw IOException("Get transactions failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getTransactionStatistics(profileId: Int): TransactionStatisticsResponse {
        val resp = service.getTransactionStatistics(profileId)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Get statistics failed: ${resp.code()} ${resp.message()}")
    }
}
