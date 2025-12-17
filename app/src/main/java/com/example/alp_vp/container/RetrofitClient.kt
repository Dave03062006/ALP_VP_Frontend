package com.example.alp_vp.container

import com.example.alp_vp.service.CurrencyApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Gunakan 10.0.2.2 jika pakai Emulator Android Studio
    // Gunakan IP Laptop (misal 192.168.1.x) jika pakai HP fisik
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val instance: CurrencyApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(CurrencyApiService::class.java)
    }
}