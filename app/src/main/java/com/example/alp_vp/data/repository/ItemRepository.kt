package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.item.CreateItemRequest
import com.example.alp_vp.data.dto.item.ItemResponse

interface ItemRepository {
    suspend fun createItem(req: CreateItemRequest): ItemResponse
    suspend fun getItems(): List<ItemResponse>
    suspend fun getItem(id: Int): ItemResponse
    suspend fun deleteItem(id: Int)
}

