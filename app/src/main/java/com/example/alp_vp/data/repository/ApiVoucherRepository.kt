package com.example.alp_vp.data.repository

import com.example.alp_vp.data.dto.voucher.Voucher
import com.example.alp_vp.data.dto.voucher.VoucherCreateRequest
import com.example.alp_vp.data.dto.voucher.VoucherPurchaseRequest
import com.example.alp_vp.data.dto.voucher.VoucherPurchaseResponse
import com.example.alp_vp.data.service.ApiService
import java.io.IOException

class ApiVoucherRepository(private val service: ApiService) : VoucherRepository {

    override suspend fun createVoucher(req: VoucherCreateRequest): Voucher {
        val resp = service.createVoucher(req)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Create voucher failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun purchaseVoucher(profileId: String, req: VoucherPurchaseRequest): VoucherPurchaseResponse {
        val resp = service.purchaseVoucher(profileId, req)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Purchase voucher failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getVouchersByGame(gameId: Int): List<Voucher> {
        val resp = service.getVouchersByGame(gameId)
        if (resp.isSuccessful) return resp.body() ?: emptyList()
        throw IOException("Get vouchers failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getVoucherPurchases(profileId: String): List<VoucherPurchaseResponse> {
        val resp = service.getVoucherPurchases(profileId)
        if (resp.isSuccessful) {
            return resp.body() ?: emptyList()
        }
        throw IOException("Get voucher purchases failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun getVoucher(id: Int): Voucher {
        val resp = service.getVoucher(id)
        if (resp.isSuccessful) return resp.body() ?: throw IOException("Empty response")
        throw IOException("Get voucher failed: ${resp.code()} ${resp.message()}")
    }

    override suspend fun useVoucherPurchase(purchaseId: Int) {
        val resp = service.useVoucherPurchase(purchaseId)
        if (!resp.isSuccessful) throw IOException("Use voucher failed: ${resp.code()} ${resp.message()}")
    }
}
