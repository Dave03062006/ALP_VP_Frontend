package com.example.alp_vp.model

data class Game(
    val id: Int,
    val title: String,
    val price: Int,
    val imageUrl: String
)

data class Voucher(
    val id: Int,
    val title: String,
    val discount: String,
    val image: String
)