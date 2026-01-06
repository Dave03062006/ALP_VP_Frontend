package com.example.alp_vp.data.dto.voucher

data class VoucherPurchaseResponse(
    val purchaseId: Int,
    val voucherId: Int,
    val profileId: String,
    val voucherName: String = "",
    val voucherValue: Double = 0.0,
    val pointsSpent: Int,
    val code: String?,
    val isUsed: Boolean,
    val purchasedAt: String
)