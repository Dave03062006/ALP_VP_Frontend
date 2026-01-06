package com.example.alp_vp.ui.model

import android.icu.util.CurrencyAmount

data class Transaction(
    val id: Int,
    val userId: Int,
    val gameId: Int,
    val transactionType: TransactionType,
    val event: Event? = null,
    val vpAmount: Int = 0,
    val isAutoMode: Boolean = true,
    val manualCost: Double? = null,
    val calculatedCost: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
