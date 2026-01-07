package com.example.alp_vp.data.dto.profile

data class ProfileStatistics(
    val totalSpent: Double,
    val totalTransactions: Int,
    val totalPointsEarned: Int,
    val vouchersPurchased: Int,
    val achievementsUnlocked: Int,
    val totalAchievements: Int,
    val savingsAmount: Double,
    val averageTransactionAmount: Double,
    val largestTransaction: Double,
    val currentStreak: Int
)

