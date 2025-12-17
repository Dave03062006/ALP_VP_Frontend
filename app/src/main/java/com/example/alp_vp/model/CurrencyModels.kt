package com.example.alp_vp.model

import com.google.gson.annotations.SerializedName

data class WebResponse<T>(
    @SerializedName("data")
    val data: T,

    @SerializedName("message")
    val message: String? = null,

    val success: Boolean? = true
)