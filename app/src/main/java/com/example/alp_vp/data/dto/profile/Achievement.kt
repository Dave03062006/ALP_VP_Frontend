package com.example.alp_vp.data.dto.profile

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean,
    val unlockedAt: String? = null,
    val progress: Double,
    val maxProgress: Double,
    val type: String,
    val rewardType: String? = null,
    val rewardValue: String? = null
)

