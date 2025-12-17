package com.example.alp_vp.data.dto.voucher

data class VoucherPurchaseResponse(
    val purchaseId: Int,
    val voucherId: Int,
    val profileId: String,
    val code: String?,       
    val used: Boolean,
    val purchasedAt: String
)