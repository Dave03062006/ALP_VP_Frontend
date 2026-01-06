package com.example.alp_vp.ui.model

data class Voucher(
    val id: String,
    val title: String,
    val cost: Int,
    val isBought: Boolean = false,
    val displayedCode: String
)
