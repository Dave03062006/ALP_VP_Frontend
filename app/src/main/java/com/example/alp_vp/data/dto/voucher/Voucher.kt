package com.example.alp_vp.data.dto.voucher

import com.google.gson.annotations.SerializedName

data class Voucher(
    @SerializedName("id")
    val id: Int,
    @SerializedName("gameId")
    val gameId: Int,
    @SerializedName("voucherName")
    val voucherName: String,
    @SerializedName("value")
    val value: Double,
    @SerializedName("pointsCost")
    val pointsCost: Int,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("isActive")
    val isActive: Boolean = true
)