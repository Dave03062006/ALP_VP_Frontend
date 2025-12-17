package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.item.ItemResponse
import com.example.alp_vp.data.dto.LoginRequest
import com.example.alp_vp.data.dto.LoginResponse
import com.example.alp_vp.data.dto.Profile
import com.example.alp_vp.data.dto.UpdateProfileRequest
import com.example.alp_vp.data.dto.RegisterRequest

interface ProfileRepository {
    suspend fun register(req: RegisterRequest): Profile
    suspend fun login(req: LoginRequest): LoginResponse
    suspend fun getLeaderboard(limit: Int = 10): List<Profile>
    suspend fun getProfile(id: Int): Profile
    suspend fun updateProfile(id: String, req: UpdateProfileRequest): Profile
    suspend fun getInventory(id: String): List<ItemResponse>
}