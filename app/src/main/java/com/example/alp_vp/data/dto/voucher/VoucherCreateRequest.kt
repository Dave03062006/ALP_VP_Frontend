package com.example.alp_vp.data.dto.voucher

data class VoucherCreateRequest(
    val gameId: Int,
    val voucherName: String,
    val value: Double,
    val pointsCost: Int,
    val stock: Int
)