package com.example.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeView() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F0FF))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            PointsCard()
            Spacer(modifier = Modifier.height(16.dp))
            GameSelector()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "All Games",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Track your gaming expenses",
                fontSize = 14.sp,
                color = Color(0xFF6B4FA0),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            StatisticsCard()
            Spacer(modifier = Modifier.height(16.dp))
            RecentTransactionsCard()
            Spacer(modifier = Modifier.height(16.dp))
            SpendingByGameCard()
            Spacer(modifier = Modifier.height(16.dp))
            StartTrackingCard()
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { /* TODO: Show popup */ },
            containerColor = Color(0xFFD946EF),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Transaction",
                tint = Color.White
            )
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeViewPreview() {
    HomeView()
}
