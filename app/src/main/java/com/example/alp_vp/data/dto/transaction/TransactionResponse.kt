package com.example.alp_vp.data.dto.transaction

data class TransactionResponse(
    val id: Int,
    val profileId: Int,
    val gameId: Int,
    val eventId: Int?,
    val transactionTypeId: Int,
    val amount: Double,
    val pointsEarned: Int,
    val purchaseDate: String,
    val game: GameInfo? = null,
    val event: EventInfo? = null,
    val transactionType: TransactionTypeInfo? = null
)

data class GameInfo(
    val id: Int,
    val name: String,
    val iconUrl: String?,
    val isActive: Boolean
)

data class EventInfo(
    val id: Int,
    val gameId: Int,
    val eventName: String,
    val isActive: Boolean
)

data class TransactionTypeInfo(
    val id: Int,
    val typeName: String,
    val pointsMultiplier: Double
)
