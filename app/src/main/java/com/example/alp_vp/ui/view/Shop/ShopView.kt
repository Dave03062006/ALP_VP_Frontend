package com.example.alp_vp.ui.view.Shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.shadow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alp_vp.VPApplication
import com.example.alp_vp.ui.viewmodel.ShopViewModel

@Composable
fun ShopView(
    onBack: () -> Unit = {},
    onNavigateToPurchaseHistory: () -> Unit = {},
    viewModel: ShopViewModel = viewModel(factory = ShopViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val application = context.applicationContext as VPApplication

    // Get user's current points from profile
    var userPoints by remember { mutableStateOf(0) }

    // Function to refresh points
    val refreshPoints: suspend () -> Unit = {
        try {
            val profile = application.container.profileRepository.getProfile(1)
            userPoints = profile.points
        } catch (e: Exception) {
            println("Error loading profile: ${e.message}")
        }
    }

    LaunchedEffect(Unit) {
        refreshPoints()
    }

    // Refresh points when purchase is successful
    LaunchedEffect(uiState.purchaseSuccess) {
        if (uiState.purchaseSuccess) {
            refreshPoints()
        }
    }

    // Show success dialog when purchase is successful
    if (uiState.purchaseSuccess) {
        AlertDialog(
            onDismissRequest = {
                viewModel.resetPurchaseSuccess()
            },
            title = { Text("Purchase Successful!") },
            text = { Text("Your voucher has been purchased successfully. View it in your purchase history.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetPurchaseSuccess()
                        onNavigateToPurchaseHistory()
                    }
                ) {
                    Text("View My Vouchers")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.resetPurchaseSuccess()
                    }
                ) {
                    Text("Close")
                }
            }
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF5F0FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 96.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Game Voucher Shop", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                    Text("Spend points on discount vouchers", fontSize = 12.sp, color = Color(0xFF6B4FA0))
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

            // Points Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE4B5)),
                border = BorderStroke(2.dp, Color(0xFFFFD27A))
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFD4AF37))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Your Points", fontWeight = FontWeight.Medium)
                    Text(userPoints.toString(), fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Earn points by tracking purchases", fontSize = 12.sp, color = Color(0xFF6B4FA0))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error message
            if (uiState.errorMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = Color(0xFFD32F2F),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Loading indicator
            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    color = Color(0xFFD946EF)
                )
            }

            // Game Selection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE0D4F7))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Select Game", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.games.isEmpty()) {
                        Text("Loading games...", fontSize = 12.sp, color = Color.Gray)
                    } else {
                        // Display games in a grid (3 columns)
                        val games = uiState.games
                        val rows = (games.size + 2) / 3

                        for (rowIndex in 0 until rows) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                for (colIndex in 0 until 3) {
                                    val index = rowIndex * 3 + colIndex
                                    if (index < games.size) {
                                        val game = games[index]
                                        GameChip(
                                            name = game.name,
                                            selected = game.id == uiState.selectedGameId,
                                            onClick = { viewModel.selectGame(game.id) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Vouchers List
            if (uiState.vouchers.isEmpty() && !uiState.isLoading) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No vouchers available",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Check back later for new offers!",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    uiState.vouchers.forEach { voucher ->
                        VoucherCard(
                            voucher = voucher,
                            userPoints = userPoints,
                            onPurchase = { viewModel.purchaseVoucher(voucher.id) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onNavigateToPurchaseHistory,
            containerColor = Color(0xFFD946EF),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "View My Vouchers",
                tint = Color.White
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun ShopViewPreview() {
    ShopView()
}