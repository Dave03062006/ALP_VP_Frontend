package com.example.alp_vp.data.container

import android.content.Context
import com.example.alp_vp.data.local.SessionManager
import com.example.alp_vp.data.repository.*
import com.example.alp_vp.data.service.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val sessionManager: SessionManager
    val authRepository: AuthRepository
    val profileRepository: ProfileRepository
    val voucherRepository: VoucherRepository
    val gameRepository: GameRepository
    val eventRepository: EventRepository
    val transactionRepository: TransactionRepository
    val itemRepository: ItemRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    // Base URL - Update this to your backend URL
    private val baseUrl = "http://10.0.2.2:3000/api/" // Added /api/ prefix to match backend routes
    // For physical device, use: "http://YOUR_IP_ADDRESS:3000/api/"

    // Session Manager
    override val sessionManager: SessionManager by lazy {
        SessionManager(context)
    }

    // Logging interceptor for debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttp client
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Retrofit instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // API Service
    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // Repositories
    override val authRepository: AuthRepository by lazy {
        ApiAuthRepository(apiService)
    }

    override val profileRepository: ProfileRepository by lazy {
        ApiProfileRepository(apiService)
    }

    override val voucherRepository: VoucherRepository by lazy {
        ApiVoucherRepository(apiService)
    }

    override val gameRepository: GameRepository by lazy {
        ApiGameRepository(apiService)
    }

    override val eventRepository: EventRepository by lazy {
        ApiEventRepository(apiService)
    }

    override val transactionRepository: TransactionRepository by lazy {
        ApiTransactionRepository(apiService)
    }

    override val itemRepository: ItemRepository by lazy {
        ApiItemRepository(apiService)
    }
}
