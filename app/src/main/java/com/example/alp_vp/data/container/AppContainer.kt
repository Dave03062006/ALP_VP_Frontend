package com.example.alp_vp.data.container

import com.example.alp_vp.data.repository.GachaRepository
import com.example.alp_vp.data.service.GachaService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {

    private val BASE_URL = "http://192.168.56.1:3000/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val gachaService: GachaService =
        retrofit.create(GachaService::class.java)

    val gachaRepository: GachaRepository =
        GachaRepository(gachaService)
}