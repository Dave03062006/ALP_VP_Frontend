package com.example.alp_vp.data.dto.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("profile")
    val profile: ProfileData,
    @SerializedName("token")
    val token: String,
    @SerializedName("message")
    val message: String
)

data class ProfileData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("displayName")
    val displayName: String?,
    @SerializedName("points")
    val points: Int,
    @SerializedName("totalSpent")
    val totalSpent: Double,
    @SerializedName("achievementCount")
    val achievementCount: Int
)

