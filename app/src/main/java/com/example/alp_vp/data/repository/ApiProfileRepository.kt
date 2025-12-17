package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.item.ItemResponse
import com.example.alp_vp.data.dto.LoginRequest
import com.example.alp_vp.data.dto.LoginResponse
import com.example.alp_vp.data.dto.Profile
import com.example.alp_vp.data.dto.UpdateProfileRequest
import com.example.alp_vp.data.dto.RegisterRequest
import com.example.alp_vp.data.service.ApiService
import java.io.IOException

class ApiProfileRepository(private val service: ApiService) : ProfileRepository {

    override suspend fun register(req: RegisterRequest): Profile {
        val resp = service.register(req)
        if (resp.isSuccessful) {
            val profileResponse = resp.body() ?: throw IOException("Empty response")
            return Profile(
                id = profileResponse.id,
                username = profileResponse.username,
                email = profileResponse.email,
                displayName = profileResponse.displayName,
                points = profileResponse.points,
                createdAt = profileResponse.createdAt
            )
        }
        throw IOException("Registration failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun login(req: LoginRequest): LoginResponse {
        val resp = service.login(req)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Login failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getLeaderboard(limit: Int): List<Profile> {
        val resp = service.leaderboard(limit)
        if (resp.isSuccessful) {
            return resp.body()?.map { profileResponse ->
                Profile(
                    id = profileResponse.id,
                    username = profileResponse.username,
                    email = profileResponse.email,
                    displayName = profileResponse.displayName,
                    points = profileResponse.points,
                    createdAt = profileResponse.createdAt
                )
            } ?: emptyList()
        }
        throw IOException("Get leaderboard failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getProfile(id: Int): Profile {
        val resp = service.getProfile(id)
        if (resp.isSuccessful) {
            val profileResponse = resp.body() ?: throw IOException("Empty response")
            return Profile(
                id = profileResponse.id,
                username = profileResponse.username,
                email = profileResponse.email,
                displayName = profileResponse.displayName,
                points = profileResponse.points,
                createdAt = profileResponse.createdAt
            )
        }
        throw IOException("Get profile failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun updateProfile(id: String, req: UpdateProfileRequest): Profile {
        val resp = service.updateProfile(id, req)
        if (resp.isSuccessful) {
            val profileResponse = resp.body() ?: throw IOException("Empty response")
            return Profile(
                id = profileResponse.id,
                username = profileResponse.username,
                email = profileResponse.email,
                displayName = profileResponse.displayName,
                points = profileResponse.points,
                createdAt = profileResponse.createdAt
            )
        }
        throw IOException("Update profile failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getInventory(id: String): List<ItemResponse> {
        val resp = service.getInventory(id)
        if (resp.isSuccessful) {
            return resp.body()?.map { inventoryItem ->
                ItemResponse(
                    id = inventoryItem.id,
                    itemName = inventoryItem.itemName,
                    rarity = inventoryItem.rarity,
                    imageUrl = inventoryItem.imageUrl,
                    isMilestone = false
                )
            } ?: emptyList()
        }
        throw IOException("Get inventory failed: ${resp.code()} ${resp.message()}")
    }
}
