package com.example.alp_vp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alp_vp.VPApplication
import com.example.alp_vp.ui.routes.BottomNavigationBar
import com.example.alp_vp.ui.routes.Screen
import com.example.alp_vp.ui.view.Home.HomeView
import com.example.alp_vp.ui.view.Shop.ShopView
import com.example.alp_vp.ui.view.Shop.PurchaseHistoryView
import com.example.alp_vp.ui.view.auth.LoginScreen
import com.example.alp_vp.ui.view.auth.RegisterScreen
import com.example.alp_vp.ui.view.gacha.GachaScreen
import com.example.alp_vp.ui.viewmodel.GachaViewModel
import com.example.alp_vp.ui.view.Profile.ProfileView

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as VPApplication
    val sessionManager = application.container.sessionManager

    var isLoggedIn by remember { mutableStateOf(sessionManager.isLoggedIn()) }
    val navController = rememberNavController()

    // Determine start destination based on login status
    val startDestination = if (isLoggedIn) "main_app" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Authentication screens
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    isLoggedIn = true
                    navController.navigate("main_app") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onRegisterSuccess = {
                    isLoggedIn = true
                    navController.navigate("main_app") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // Main app with bottom navigation
        composable("main_app") {
            MainAppScreen(
                onLogout = {
                    sessionManager.logout()
                    isLoggedIn = false
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun MainAppScreen(onLogout: () -> Unit) {
    val context = LocalContext.current
    val application = context.applicationContext as VPApplication
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
                val gachaViewModel = remember {
                    GachaViewModel(application.container.gachaRepository)
                }
                GachaScreen(viewModel = gachaViewModel)
            }
            composable(Screen.History.route) {
                // TODO: HistoryView()
            }
            composable(Screen.Profile.route) {
                ProfileView(navController = navController)
            }
            composable(Screen.Shop.route) {
                ShopView(
                    onBack = { navController.navigateUp() },
                    onNavigateToPurchaseHistory = { navController.navigate(Screen.PurchaseHistory.route) }
                )
            }
            composable(Screen.PurchaseHistory.route) {
                PurchaseHistoryView(
                    onBack = { navController.navigateUp() }
                )
            }
        }
    }
}
