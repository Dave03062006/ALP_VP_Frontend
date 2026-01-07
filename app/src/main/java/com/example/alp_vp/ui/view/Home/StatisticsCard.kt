package com.example.alp_vp.ui.view.Home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alp_vp.data.dto.transaction.TransactionStatisticsResponse
import com.example.alp_vp.ui.viewmodel.GameSpending

@Composable
fun StatisticsCard(
    statistics: TransactionStatisticsResponse? = null,
    topGame: GameSpending? = null
) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = null,
                        tint = Color(0xFFD946EF)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Statistics",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Total Spent",
                        value = "Rp ${"%,.0f".format(statistics?.totalSpent ?: 0.0)}",
                        icon = Icons.Default.AttachMoney,
                        backgroundColor = Color(0xFFE9D5FF),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Total Points Earned",
                        value = "${String.format("%.0f", statistics?.totalEarned ?: 0.0)} pts",
                        icon = Icons.Default.Star,
                        backgroundColor = Color(0xFFBFDBFE),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Transactions",
                        value = "${statistics?.transactionsCount ?: 0}",
                        icon = Icons.Default.SwapHoriz,
                        backgroundColor = Color(0xFFA7F3D0),
                        modifier = Modifier.weight(1f)
                    )
                    // Replace "Points" with "Top Game"
                    StatCard(
                        title = "Top Game",
                        value = topGame?.gameName ?: "No data",
                        icon = Icons.Default.Gamepad,
                        backgroundColor = Color(0xFFFED7AA),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF6B4FA0)
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color(0xFF6B4FA0)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
