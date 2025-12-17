package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.item.CreateItemRequest
import com.example.alp_vp.data.dto.item.ItemResponse
import com.example.alp_vp.data.service.ApiService
import java.io.IOException

class ApiItemRepository(private val service: ApiService) : ItemRepository {

    override suspend fun createItem(req: CreateItemRequest): ItemResponse {
        val resp = service.createItem(req)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Create item failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getItems(): List<ItemResponse> {
        val resp = service.getItems()
        if (resp.isSuccessful) return resp.body() ?: emptyList()
        throw IOException("Get items failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getItem(id: Int): ItemResponse {
        val resp = service.getItem(id)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Get item failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun deleteItem(id: Int) {
        val resp = service.deleteItem(id)
        if (!resp.isSuccessful) throw IOException("Delete item failed: ${resp.code()} ${resp.message()}")
    }
}

