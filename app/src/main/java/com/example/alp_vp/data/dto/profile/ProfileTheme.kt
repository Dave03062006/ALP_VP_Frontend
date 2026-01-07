package com.example.alp_vp.data.dto.profile

data class ProfileTheme(
    val id: String,
    val name: String,
    val primaryColor: String,
    val secondaryColor: String,
    val accentColor: String,
    val isUnlocked: Boolean,
    val requiredAchievements: Int
)

