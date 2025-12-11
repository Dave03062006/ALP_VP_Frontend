package com.example.alp_vp.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.filled.Email
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp

@Composable
fun ShopView(onBack: () -> Unit = {}) {
    var selectedGame by remember { mutableStateOf("Valorant") }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF5F0FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 96.dp) // leave room for FAB
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

            // Points card (simple version reusing visual style)
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE4B5)),
                border = BorderStroke(2.dp, Color(0xFFFFD27A))
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFD4AF37))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Your Points", fontWeight = FontWeight.Medium)
                    Text("0", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Earn points by tracking purchases", fontSize = 12.sp, color = Color(0xFF6B4FA0))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Game selector grid
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE0D4F7))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Select Game", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    val games = listOf("Valorant", "Genshin Impact", "Honkai Star Rail", "League of Legends", "Apex Legends", "Roblox")
                    // 2 rows of 3 items
                    for (rowIndex in 0 until 2) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            for (colIndex in 0 until 3) {
                                val index = rowIndex * 3 + colIndex
                                if (index < games.size) {
                                    GameChip(
                                        name = games[index],
                                        selected = games[index] == selectedGame,
                                        onClick = { selectedGame = games[index] },
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

            Spacer(modifier = Modifier.height(16.dp))

            // Voucher list
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                VoucherCard(
                    title = "10% VP Discount",
                    subtitle = "Get 10% off your next VP purchase",
                    costPoints = 50,
                    rightLabel = "10% OFF"
                )
                VoucherCard(
                    title = "20% VP Discount",
                    subtitle = "Get 20% off your next VP purchase",
                    costPoints = 100,
                    rightLabel = "20% OFF"
                )
                VoucherCard(
                    title = "Battle Pass Voucher",
                    subtitle = "$5 off Battle Pass",
                    costPoints = 75,
                    rightLabel = "$5 OFF"
                )
            }
        }

        // Floating Action Button - same design as HomeView
        FloatingActionButton(
            onClick = { /* TODO: Quick purchase or help */ },
            containerColor = Color(0xFFD946EF),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun GameChip(name: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val bg = if (selected) Color(0xFFEBD7FF) else Color.White
    val borderColor = if (selected) Color(0xFF9C6FDE) else Color(0xFFEDD8FF)
    Card(
        modifier = modifier
            .height(72.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bg),
        border = BorderStroke(2.dp, borderColor)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(name, textAlign = TextAlign.Center, fontSize = 12.sp, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
private fun VoucherCard(title: String, subtitle: String, costPoints: Int, rightLabel: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE4B5)),
        border = BorderStroke(2.dp, Color(0xFFFFC457))
    ) {
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF8B4513))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(title, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(subtitle, fontSize = 12.sp, color = Color(0xFF6B4FA0))
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFF8B4513), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("$costPoints Points", fontSize = 12.sp)
                }
            }

            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.SpaceBetween) {
                Text(rightLabel, color = Color(0xFF0FA97A), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* TODO: handle purchase */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA46D)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Purchase", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun ShopViewPreview() {
    ShopView()
}