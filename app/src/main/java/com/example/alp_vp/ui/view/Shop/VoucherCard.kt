package com.example.alp_vp.ui.view.Shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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


@Composable
fun VoucherCard(title: String, subtitle: String, costPoints: Int, rightLabel: String) {
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

@Composable
@Preview(showBackground = true)
fun VoucherCardPreview() {
    VoucherCard(
        title = "Steam Voucher",
        subtitle = "Get a $10 Steam voucher",
        costPoints = 100,
        rightLabel = "Available"
    )
}