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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.alp_vp.VPApplication
import com.example.alp_vp.ui.view.TransactionDialog
import com.example.alp_vp.ui.viewmodel.HomeViewModel
import com.example.alp_vp.ui.routes.Screen

@Composable
fun HomeView(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var showTransactionDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val application = context.applicationContext as VPApplication

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F0FF))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            // Show error message if there's an error
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

            // Show loading indicator
            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    color = Color(0xFFD946EF)
                )
            }

            // Points Card at the top (restored original position)
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
            //CurrencyCalculatorView()
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
            // Statistics Card now shows Top Game instead of Points
            StatisticsCard(
                statistics = uiState.statistics,
                topGame = uiState.gameSpendingRanking.firstOrNull()
            )
            Spacer(modifier = Modifier.height(16.dp))
            RecentTransactionsCard(transactions = uiState.transactions)
            Spacer(modifier = Modifier.height(16.dp))
            SpendingByGameCard(gameSpendingRanking = uiState.gameSpendingRanking)
            Spacer(modifier = Modifier.height(16.dp))
            StartTrackingCard(onAddClick = { showTransactionDialog = true })
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showTransactionDialog = true },
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

    if(showTransactionDialog){
        TransactionDialog(
            profileId = application.container.sessionManager.getUserId(),
            eventRepository = application.container.eventRepository,
            onDismiss = { showTransactionDialog = false },
            onConfirm = { transactionRequest ->
                viewModel.createTransaction(transactionRequest)
                showTransactionDialog = false
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeViewPreview() {
    HomeView(navController = rememberNavController())
}
