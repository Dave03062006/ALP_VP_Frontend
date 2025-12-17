package com.example.alp_vp.ui.model

import java.util.Date

data class Profile(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val bio: String,
    val profilePictureId: Int?,
    val points: Int,
    val totalSpent: Float,
//    val achievements: List<Achievements>
    val dateCreated: Date
)
