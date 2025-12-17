package com.example.alp_vp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alp_vp.ui.routes.BottomNavigationBar
import com.example.alp_vp.ui.routes.Screen
import com.example.alp_vp.ui.view.Home.HomeView
import com.example.alp_vp.ui.view.Shop.ShopView

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeView(navController)
            }
            composable(Screen.Calculator.route) {
                // TODO: CurrencyCalculatorView()
            }
            composable(Screen.Gacha.route) {
                // TODO: GachaView()
            }
            composable(Screen.History.route) {
                // TODO: HistoryView()
            }
            composable(Screen.Profile.route) {
                // TODO: ProfileView()
            }
            composable(Screen.Shop.route) {
                ShopView(onBack = { navController.navigateUp() })
            }
        }
    }
}
