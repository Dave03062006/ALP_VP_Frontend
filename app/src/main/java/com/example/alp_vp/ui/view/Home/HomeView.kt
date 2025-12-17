package com.example.alp_vp.ui.view.Home

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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.alp_vp.ui.routes.Screen
import com.example.alp_vp.ui.view.TransactionDialog
import com.example.alp_vp.ui.viewmodel.HomeViewModel

@Composable
fun HomeView(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var showTransactionDialog by remember { mutableStateOf(false) }

    // Refresh data when screen becomes visible
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F0FF))
                .blur(if (showTransactionDialog) 10.dp else 0.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            // Refresh Button at the top
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { viewModel.loadData() },
                    enabled = !uiState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color(0xFFD946EF)
                    )
                }
            }

            PointsCard(
                points = uiState.userPoints,
                onShopClick = { navController.navigate(Screen.Shop.route) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameSelector(
                games = uiState.games,
                selectedGame = uiState.selectedGame,
                onGameSelected = { game -> viewModel.selectGame(game) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = uiState.selectedGame?.name ?: "All Games",
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
            StatisticsCard(statistics = uiState.statistics)
            Spacer(modifier = Modifier.height(16.dp))
            RecentTransactionsCard(transactions = uiState.transactions)
            Spacer(modifier = Modifier.height(16.dp))
            SpendingByGameCard()
            Spacer(modifier = Modifier.height(16.dp))
            StartTrackingCard(onAddClick = { showTransactionDialog = true })

            // Show loading indicator
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }

            // Show error message prominently
            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Error Loading Data",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD32F2F)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = error,
                            fontSize = 14.sp,
                            color = Color(0xFFD32F2F)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Make sure your backend is running on http://localhost:3000",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadData() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Retry")
                        }
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showTransactionDialog = true },
            containerColor = Color(0xFFD946EF),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 64.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Transaction",
                tint = Color.White
            )
        }
    }

    if (showTransactionDialog) {
        TransactionDialog(
            onDismiss = { showTransactionDialog = false },
            onConfirm = { request ->
                viewModel.createTransaction(request)
                showTransactionDialog = false
            },
            profileId = 1
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun HomeViewPreview() {
////    HomeView()
//}
