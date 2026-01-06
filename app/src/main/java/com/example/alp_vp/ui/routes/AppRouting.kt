package com.example.alp_vp.ui.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Calculator : Screen("calculator")
    object Gacha : Screen("gacha")
    object History : Screen("history")
    object Profile : Screen("profile")
    object Shop : Screen("shop")
    object PurchaseHistory : Screen("purchase_history")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Calculator", Screen.Calculator.route, Icons.Default.Calculate),
        BottomNavItem("Gacha", Screen.Gacha.route, Icons.Default.Stars),
        BottomNavItem("History", Screen.History.route, Icons.Default.History),
        BottomNavItem("Profile", Screen.Profile.route, Icons.Default.Person)
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFD946EF),
                    selectedTextColor = Color(0xFFD946EF),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFFF5F0FF)
                )
            )
        }
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: ImageVector)
