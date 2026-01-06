package com.example.alp_vp.ui.view.Shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alp_vp.VPApplication
import com.example.alp_vp.data.dto.voucher.VoucherPurchaseResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PurchaseHistoryView(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as VPApplication
    val coroutineScope = rememberCoroutineScope()

    var purchases by remember { mutableStateOf<List<VoucherPurchaseResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Load purchase history
    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = null
        try {
            val history = application.container.voucherRepository.getVoucherPurchases("1")
            purchases = history
        } catch (e: Exception) {
            errorMessage = e.message ?: "Failed to load purchase history"
            println("Error loading purchases: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F0FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "My Vouchers",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                    Text(
                        "Your purchased discount vouchers",
                        fontSize = 12.sp,
                        color = Color(0xFF6B4FA0)
                    )
                }

                OutlinedButton(
                    onClick = onBack,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF9C6FDE)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF9C6FDE))
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Back")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error message
            if (errorMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFD32F2F),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Loading indicator
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFD946EF))
                }
            } else if (purchases.isEmpty()) {
                // Empty state
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Vouchers Yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Purchase vouchers from the shop to see them here",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Purchase list
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(purchases) { purchase ->
                        PurchaseCard(purchase = purchase)
                    }
                }
            }
        }
    }
}

@Composable
fun PurchaseCard(purchase: VoucherPurchaseResponse) {
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()
    var showCopied by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.US)
    val formattedDate = try {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).parse(purchase.purchasedAt)
        if (date != null) dateFormat.format(date) else purchase.purchasedAt
    } catch (e: Exception) {
        purchase.purchasedAt
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE4B5)),
        border = BorderStroke(2.dp, Color(0xFFFFC457))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with voucher name and used status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(0xFF8B4513)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        purchase.voucherName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }

                if (purchase.isUsed) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF4CAF50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Used",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Voucher details
            Text(
                "Discount: $${String.format(Locale.US, "%.2f", purchase.voucherValue)}",
                fontSize = 13.sp,
                color = Color(0xFF6B4FA0)
            )

            Text(
                "Points Spent: ${purchase.pointsSpent}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Voucher Code
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFD946EF))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Voucher Code",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            purchase.code ?: "N/A",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD946EF),
                            letterSpacing = 2.sp
                        )
                    }

                    IconButton(
                        onClick = {
                            purchase.code?.let { code ->
                                clipboardManager.setText(AnnotatedString(code))
                                showCopied = true
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(2000)
                                    showCopied = false
                                }
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copy code",
                            tint = Color(0xFF9C6FDE)
                        )
                    }
                }
            }

            if (showCopied) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "✓ Code copied to clipboard",
                    fontSize = 11.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Purchase date
            Text(
                "Purchased: $formattedDate",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

