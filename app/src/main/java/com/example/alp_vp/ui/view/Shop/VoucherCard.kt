package com.example.alp_vp.ui.view.Shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alp_vp.data.dto.voucher.Voucher

@Composable
fun VoucherCard(
    voucher: Voucher,
    userPoints: Int,
    onPurchase: () -> Unit
) {
    // Debug logging with null safety
    try {
        println("VoucherCard: Rendering voucher")
        println("VoucherCard: ID=${voucher.id}, Name='${voucher.voucherName}'")
        println("VoucherCard: value=${voucher.value}, pointsCost=${voucher.pointsCost}, stock=${voucher.stock}")
    } catch (e: Exception) {
        println("VoucherCard: Error logging voucher data: ${e.message}")
    }

    val hasEnoughPoints = userPoints >= voucher.pointsCost
    val isOutOfStock = voucher.stock == 0
    val stockText = when {
        voucher.stock == -1 -> "Unlimited"
        voucher.stock == 0 -> "Out of Stock"
        voucher.stock < 10 -> "Only ${voucher.stock} left!"
        else -> "In Stock"
    }

    val voucherNameText = voucher.voucherName.takeIf { it.isNotBlank() } ?: "Unknown Voucher"
    val discountText = "Get $${String.format("%.2f", voucher.value)} discount"

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isOutOfStock) Color(0xFFE0E0E0) else Color(0xFFFFE4B5)
        ),
        border = BorderStroke(
            2.dp,
            if (isOutOfStock) Color.Gray else Color(0xFFFFC457)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left side - Voucher info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = if (isOutOfStock) Color.Gray else Color(0xFF8B4513),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = voucherNameText,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isOutOfStock) Color.Gray else Color.Black,
                        fontSize = 14.sp,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = discountText,
                    fontSize = 12.sp,
                    color = if (isOutOfStock) Color.Gray else Color(0xFF6B4FA0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = if (isOutOfStock) Color.Gray else Color(0xFF8B4513),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${voucher.pointsCost} Points",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isOutOfStock) Color.Gray else Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right side - Stock and Purchase button
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = stockText,
                    color = when {
                        isOutOfStock -> Color.Gray
                        voucher.stock in 1..9 -> Color(0xFFFF6B6B)
                        else -> Color(0xFF0FA97A)
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(
                    onClick = onPurchase,
                    enabled = hasEnoughPoints && !isOutOfStock,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFA46D),
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = when {
                            isOutOfStock -> "Sold Out"
                            !hasEnoughPoints -> "Need Points"
                            else -> "Purchase"
                        },
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun VoucherCardPreview() {
    VoucherCard(
        voucher = Voucher(
            id = 1,
            gameId = 1,
            voucherName = "10% Discount",
            value = 10.0,
            pointsCost = 100,
            stock = 50
        ),
        userPoints = 150,
        onPurchase = {}
    )
}