// File: `app/src/main/java/com/example/alp_vp/data/service/ApiService.kt`
package com.example.alp_vp.data.service

import com.example.alp_vp.data.dto.*
import com.example.alp_vp.data.dto.auth.AuthResponse
import com.example.alp_vp.data.dto.auth.LoginRequest
import com.example.alp_vp.data.dto.auth.RegisterRequest
import com.example.alp_vp.data.dto.item.CreateItemRequest
import com.example.alp_vp.data.dto.item.InventoryItemResponse
import com.example.alp_vp.data.dto.item.ItemResponse
import com.example.alp_vp.data.dto.transaction.CreateTransactionRequest
import com.example.alp_vp.data.dto.transaction.TransactionResponse
import com.example.alp_vp.data.dto.transaction.TransactionStatisticsResponse
import com.example.alp_vp.data.dto.voucher.Voucher
import com.example.alp_vp.data.dto.voucher.VoucherCreateRequest
import com.example.alp_vp.data.dto.voucher.VoucherPurchaseRequest
import com.example.alp_vp.data.dto.voucher.VoucherPurchaseResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Authentication
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    // Profiles
    @GET("profiles/leaderboard")
    suspend fun leaderboard(@Query("limit") limit: Int? = 10): Response<List<ProfileResponse>>

    @GET("profiles/{id}")
    suspend fun getProfile(@Path("id") id: Int): Response<ProfileResponse>

    @PUT("profiles/{id}")
    suspend fun updateProfile(@Path("id") id: String, @Body body: UpdateProfileRequest): Response<ProfileResponse>

    @GET("profiles/{id}/inventory")
    suspend fun getInventory(@Path("id") id: String): Response<List<InventoryItemResponse>>

    // Games
    @POST("games")
    suspend fun createGame(@Body body: CreateGameRequest): Response<GameResponse>

    @PUT("games/{id}")
    suspend fun updateGame(@Path("id") id: Int, @Body body: UpdateGameRequest): Response<GameResponse>

    @GET("games")
    suspend fun getGames(@Query("active") onlyActive: Boolean? = null): Response<List<GameResponse>>

    @GET("games/{id}")
    suspend fun getGame(@Path("id") id: Int): Response<GameResponse>

    @DELETE("games/{id}")
    suspend fun deleteGame(@Path("id") id: Int): Response<Unit>

    // Events
    @POST("events")
    suspend fun createEvent(@Body body: CreateEventRequest): Response<EventResponse>

    @PUT("events/{id}")
    suspend fun updateEvent(@Path("id") id: Int, @Body body: UpdateEventRequest): Response<EventResponse>

    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id: Int): Response<EventResponse>

    @GET("events/by-game/{gameId}")
    suspend fun getEventsByGame(@Path("gameId") gameId: Int): Response<List<EventResponse>>

    @DELETE("events/{id}")
    suspend fun deleteEvent(@Path("id") id: Int): Response<Unit>

    // Items
    @POST("items")
    suspend fun createItem(@Body body: CreateItemRequest): Response<ItemResponse>

    @GET("items")
    suspend fun getItems(): Response<List<ItemResponse>>

    @GET("items/{id}")
    suspend fun getItem(@Path("id") id: Int): Response<ItemResponse>

    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: Int): Response<Unit>

    // Transactions
    @POST("profiles/{profileId}/transactions")
    suspend fun createTransaction(
        @Path("profileId") profileId: Int,
        @Body request: CreateTransactionRequest
    ): Response<TransactionResponse>

    @GET("profiles/{profileId}/transactions")
    suspend fun getTransactions(
        @Path("profileId") profileId: Int,
        @Query("gameId") gameId: Int? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Response<List<TransactionResponse>>

    @GET("profiles/{profileId}/transactions/statistics")
    suspend fun getTransactionStatistics(
        @Path("profileId") profileId: Int
    ): Response<TransactionStatisticsResponse>

    // Vouchers
    @POST("vouchers")
    suspend fun createVoucher(@Body body: VoucherCreateRequest): Response<Voucher>

    @POST("profiles/{profileId}/vouchers/purchase")
    suspend fun purchaseVoucher(@Path("profileId") profileId: String, @Body body: VoucherPurchaseRequest): Response<VoucherPurchaseResponse>

    @GET("vouchers/by-game")
    suspend fun getVouchersByGame(@Query("gameId") gameId: Int): Response<List<Voucher>>

    @GET("profiles/{profileId}/vouchers/purchases")
    suspend fun getVoucherPurchases(@Path("profileId") profileId: String): Response<List<VoucherPurchaseResponse>>

    @GET("vouchers/{id}")
    suspend fun getVoucher(@Path("id") id: Int): Response<Voucher>

    @POST("vouchers/purchases/{id}/use")
    suspend fun useVoucherPurchase(@Path("id") purchaseId: Int): Response<Unit>
}
