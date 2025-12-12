package com.example.alp_vp.ui.view.Shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameChip(name: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
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
@Preview(showBackground = true)
fun GameChipPreview() {
    GameChip(name = "Game Title", selected = true, onClick = {})
}