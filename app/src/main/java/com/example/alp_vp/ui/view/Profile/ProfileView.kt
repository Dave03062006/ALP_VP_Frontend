package com.example.alp_vp.ui.view.Profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.alp_vp.data.dto.profile.Achievement
import com.example.alp_vp.data.dto.profile.ProfileTheme
import com.example.alp_vp.ui.viewmodel.ProfileViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    // Get theme colors
    val themeColor = remember(uiState.selectedTheme) {
        try {
            Color(android.graphics.Color.parseColor(uiState.selectedTheme?.primaryColor ?: "#9C6FDE"))
        } catch (_: Exception) {
            Color(0xFF9C6FDE)
        }
    }

    val secondaryColor = remember(uiState.selectedTheme) {
        try {
            Color(android.graphics.Color.parseColor(uiState.selectedTheme?.secondaryColor ?: "#D946EF"))
        } catch (_: Exception) {
            Color(0xFFD946EF)
        }
    }

    val accentColor = remember(uiState.selectedTheme) {
        try {
            Color(android.graphics.Color.parseColor(uiState.selectedTheme?.accentColor ?: "#F5F0FF"))
        } catch (_: Exception) {
            Color(0xFFF5F0FF)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = themeColor,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { showThemeDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Change Theme",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(accentColor)
                .padding(paddingValues)
        ) {
            if (uiState.isLoading && uiState.profile == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = themeColor,
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading your profile...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Error message
                    if (uiState.errorMessage != null) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFD32F2F)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = uiState.errorMessage ?: "",
                                        color = Color(0xFFD32F2F),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    // Profile Header
                    item {
                        ProfileHeaderCard(
                            profile = uiState.profile,
                            isEditMode = uiState.isEditMode,
                            editedDisplayName = uiState.editedDisplayName,
                            themeColor = themeColor,
                            secondaryColor = secondaryColor,
                            onEditClick = { viewModel.toggleEditMode() },
                            onDisplayNameChange = { viewModel.updateDisplayName(it) },
                            onSaveClick = { viewModel.saveProfile() },
                            onThemeClick = { showThemeDialog = true }
                        )
                    }

                    // Statistics Section
                    item {
                        StatisticsSection(
                            statistics = uiState.statistics,
                            themeColor = themeColor,
                            secondaryColor = secondaryColor
                        )
                    }

                    // Achievements Section
                    item {
                        AchievementsSection(
                            achievements = uiState.achievements,
                            themeColor = themeColor,
                            secondaryColor = secondaryColor
                        )
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = themeColor,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    "Logout",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Are you sure you want to logout? You can always sign back in later.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.logout()
                        showLogoutDialog = false
                        onLogout()  // Call the logout callback to navigate back to login
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = themeColor)
                ) {
                    Text("Yes, Logout")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Theme Selection Dialog
    if (showThemeDialog) {
        ThemeSelectionDialog(
            themes = uiState.themes,
            selectedTheme = uiState.selectedTheme,
            currentThemeColor = themeColor,
            onThemeSelected = { theme ->
                viewModel.selectTheme(theme)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
}

@Composable
fun ProfileHeaderCard(
    profile: com.example.alp_vp.data.dto.Profile?,
    isEditMode: Boolean,
    editedDisplayName: String,
    themeColor: Color,
    secondaryColor: Color,
    onEditClick: () -> Unit,
    onDisplayNameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onThemeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture with gradient border
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(themeColor, secondaryColor)
                        )
                    )
                    .padding(3.dp)  // Reduced from 4.dp
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onThemeClick() },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(themeColor.copy(alpha = 0.9f), secondaryColor)  // Increased from 0.8f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile?.displayName?.firstOrNull()?.uppercase()
                            ?: profile?.username?.firstOrNull()?.uppercase()
                            ?: "?",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display Name (Editable)
            if (isEditMode) {
                OutlinedTextField(
                    value = editedDisplayName,
                    onValueChange = onDisplayNameChange,
                    label = { Text("Display Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = themeColor,
                        focusedLabelColor = themeColor,
                        cursorColor = themeColor
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            } else {
                Text(
                    text = profile?.displayName ?: profile?.username ?: "Unknown",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = "@${profile?.username ?: "unknown"}",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Member Since with icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = themeColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Member since ${formatDate(profile?.createdAt)}",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Points Display with gradient background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                themeColor.copy(alpha = 0.15f),
                                secondaryColor.copy(alpha = 0.15f)
                            )
                        )
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(themeColor, secondaryColor)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${profile?.points ?: 0}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = themeColor
                        )
                        Text(
                            text = "Total Points",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Edit/Save Button
            if (isEditMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onEditClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, Color.LightGray)
                    ) {
                        Text("Cancel", fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = onSaveClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = themeColor
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = themeColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun StatisticsSection(
    statistics: com.example.alp_vp.data.dto.profile.ProfileStatistics?,
    themeColor: Color,
    secondaryColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = themeColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Statistics",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Grid of statistics
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatisticItem(
                        icon = Icons.Default.ShoppingCart,
                        label = "Total Spent",
                        value = formatCurrency(statistics?.totalSpent ?: 0.0),
                        themeColor = themeColor,
                        modifier = Modifier.weight(1f)
                    )
                    StatisticItem(
                        icon = Icons.AutoMirrored.Filled.List,
                        label = "Transactions",
                        value = "${statistics?.totalTransactions ?: 0}",
                        themeColor = themeColor,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatisticItem(
                        icon = Icons.Default.Star,
                        label = "Points Earned",
                        value = "${statistics?.totalPointsEarned ?: 0}",
                        themeColor = secondaryColor,
                        modifier = Modifier.weight(1f)
                    )
                    StatisticItem(
                        icon = Icons.Default.Favorite,
                        label = "Vouchers",
                        value = "${statistics?.vouchersPurchased ?: 0}",
                        themeColor = Color(0xFFEC4899),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Highlighted savings stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatisticItem(
                        icon = Icons.Default.AccountBalance,
                        label = "Total Savings",
                        value = formatCurrency(statistics?.savingsAmount ?: 0.0),
                        themeColor = Color(0xFF10B981),
                        modifier = Modifier.weight(1f),
                        highlight = true
                    )
                    StatisticItem(
                        icon = Icons.Default.DateRange,
                        label = "Saving Streak",
                        value = "${statistics?.currentStreak ?: 0}d",
                        themeColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f),
                        highlight = true
                    )
                }
            }
        }
    }
}

@Composable
fun StatisticItem(
    icon: ImageVector,
    label: String,
    value: String,
    themeColor: Color,
    modifier: Modifier = Modifier,
    highlight: Boolean = false
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (highlight)
                themeColor.copy(alpha = 0.12f)
            else
                Color(0xFFF9FAFB)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (highlight) 2.dp else 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(themeColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = themeColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF1F2937)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AchievementsSection(
    achievements: List<Achievement>,
    themeColor: Color,
    secondaryColor: Color
) {
    val unlockedCount = achievements.count { it.isUnlocked }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = themeColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Achievements",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                }
                // Progress badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(themeColor, secondaryColor)
                            )
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "$unlockedCount/${achievements.size}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar
            val progress by animateFloatAsState(
                targetValue = if (achievements.isNotEmpty()) unlockedCount.toFloat() / achievements.size else 0f,
                animationSpec = tween(durationMillis = 1000),
                label = "progress"
            )

            Column {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = themeColor,
                    trackColor = Color(0xFFE5E7EB)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${(progress * 100).toInt()}% Complete • Keep saving to unlock more rewards!",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Achievement List
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                achievements.forEach { achievement ->
                    AchievementItem(
                        achievement = achievement,
                        themeColor = themeColor,
                        secondaryColor = secondaryColor
                    )
                }
            }
        }
    }
}

@Composable
fun AchievementItem(
    achievement: Achievement,
    themeColor: Color,
    secondaryColor: Color
) {
    val progressPercent = (achievement.progress / achievement.maxProgress * 100).toInt()
    val animatedProgress by animateFloatAsState(
        targetValue = (achievement.progress / achievement.maxProgress).toFloat(),
        animationSpec = tween(durationMillis = 800),
        label = "achievement_progress"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked)
                themeColor.copy(alpha = 0.1f)
            else
                Color(0xFFF9FAFB)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (achievement.isUnlocked) 1.dp else 0.dp  // Reduced from 2.dp to 1.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Achievement Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (achievement.isUnlocked) {
                            Brush.linearGradient(
                                colors = listOf(themeColor, secondaryColor)
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFD1D5DB), Color(0xFF9CA3AF))
                            )
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getAchievementIcon(achievement.type),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Achievement Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = achievement.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    if (achievement.isUnlocked) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(themeColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Unlocked",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = achievement.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )

                // Progress Bar
                if (!achievement.isUnlocked) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column {
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = themeColor,
                            trackColor = Color(0xFFE5E7EB)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$progressPercent% • ${achievement.progress.toInt()}/${achievement.maxProgress.toInt()}",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else if (achievement.rewardType == "theme") {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = themeColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Theme Unlocked!",
                            fontSize = 11.sp,
                            color = themeColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeSelectionDialog(
    themes: List<ProfileTheme>,
    selectedTheme: ProfileTheme?,
    currentThemeColor: Color,
    onThemeSelected: (ProfileTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    "Choose Your Theme",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    "Unlock more by achieving goals!",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal
                )
            }
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(450.dp)
            ) {
                items(themes) { theme ->
                    ThemeOption(
                        theme = theme,
                        isSelected = theme.id == selectedTheme?.id,
                        onClick = { if (theme.isUnlocked) onThemeSelected(theme) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = currentThemeColor
                )
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun ThemeOption(
    theme: ProfileTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val themeColor = try {
        Color(android.graphics.Color.parseColor(theme.primaryColor))
    } catch (_: Exception) {
        Color.Gray
    }

    val secondaryColor = try {
        Color(android.graphics.Color.parseColor(theme.secondaryColor))
    } catch (_: Exception) {
        Color.Gray
    }

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) themeColor else Color.Transparent,
        label = "border"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .border(0.5.dp, borderColor, RoundedCornerShape(16.dp))  // Reduced from 3.dp
            .clickable(enabled = theme.isUnlocked, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (theme.isUnlocked) Color.White else Color(0xFFF3F4F6)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (theme.isUnlocked) 2.dp else 0.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(12.dp)
            ) {
                // Color preview circle
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            if (theme.isUnlocked) {
                                Brush.linearGradient(
                                    colors = listOf(themeColor, secondaryColor)
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFD1D5DB), Color(0xFF9CA3AF))
                                )
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (!theme.isUnlocked) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    } else if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = theme.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    color = if (theme.isUnlocked) Color(0xFF1F2937) else Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (!theme.isUnlocked) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE5E7EB))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Need ${theme.requiredAchievements}",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

fun getAchievementIcon(type: String): ImageVector {
    return when (type) {
        "savings" -> Icons.Default.AccountBalance
        "spending" -> Icons.Default.ShoppingCart
        "transaction" -> Icons.AutoMirrored.Filled.List
        "voucher" -> Icons.Default.Favorite
        "milestone" -> Icons.Default.Star
        else -> Icons.Default.Check
    }
}

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount)
}

fun formatDate(dateString: String?): String {
    if (dateString.isNullOrEmpty()) return "Unknown"
    return try {
        val date = dateString.split("T")[0]
        date
    } catch (_: Exception) {
        "Unknown"
    }
}
