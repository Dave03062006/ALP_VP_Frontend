package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.auth.AuthResponse
import com.example.alp_vp.data.dto.auth.LoginRequest
import com.example.alp_vp.data.dto.auth.RegisterRequest

interface AuthRepository {
    suspend fun register(request: RegisterRequest): AuthResponse
    suspend fun login(request: LoginRequest): AuthResponse
}

