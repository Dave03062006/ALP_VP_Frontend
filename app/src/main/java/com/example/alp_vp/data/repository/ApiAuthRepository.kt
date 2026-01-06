package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.auth.AuthResponse
import com.example.alp_vp.data.dto.auth.LoginRequest
import com.example.alp_vp.data.dto.auth.RegisterRequest
import com.example.alp_vp.data.service.ApiService
import java.io.IOException

class ApiAuthRepository(private val service: ApiService) : AuthRepository {

    override suspend fun register(request: RegisterRequest): AuthResponse {
        val resp = service.register(request)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Registration failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun login(request: LoginRequest): AuthResponse {
        val resp = service.login(request)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Login failed: ${resp.code()} ${resp.message()}")
    }
}

