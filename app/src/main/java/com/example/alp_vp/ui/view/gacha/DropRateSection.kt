package com.example.alp_vp.ui.view.gacha

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DropRateSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Drop Rates",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        DropRateRow("Legendary", "2%")
        DropRateRow("Epic", "10%")
        DropRateRow("Rare", "25%")
        DropRateRow("Common", "63%")
    }
}

@Composable
private fun DropRateRow(
    rarity: String,
    rate: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = rarity)
        Text(text = rate)
    }
}

@Preview(showBackground = true)
@Composable
fun DropRateSectionPreview() {
    DropRateSection()
}
