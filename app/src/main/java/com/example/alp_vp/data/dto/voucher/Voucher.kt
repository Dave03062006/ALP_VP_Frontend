package com.example.alp_vp.data.dto.voucher

data class Voucher(
    val id: Int,
    val gameId: Int,
    val voucherName: String,
    val value: Double,
    val pointsCost: Int,
    val stock: Int,
    val code: String? = null,
    val isCodeHidden: Boolean = true,
    val createdAt: String
)