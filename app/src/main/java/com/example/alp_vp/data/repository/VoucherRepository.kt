package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.voucher.Voucher
import com.example.alp_vp.data.dto.voucher.VoucherCreateRequest
import com.example.alp_vp.data.dto.voucher.VoucherPurchaseRequest
import com.example.alp_vp.data.dto.voucher.VoucherPurchaseResponse

interface VoucherRepository {
    suspend fun createVoucher(req: VoucherCreateRequest): Voucher
    suspend fun purchaseVoucher(profileId: String, req: VoucherPurchaseRequest): VoucherPurchaseResponse
    suspend fun getVouchersByGame(gameId: Int): List<Voucher>
    suspend fun getVoucherPurchases(profileId: String): List<VoucherPurchaseResponse>
    suspend fun getVoucher(id: Int): Voucher
    suspend fun useVoucherPurchase(purchaseId: Int)
}