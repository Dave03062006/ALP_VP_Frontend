package com.example.alp_vp.ui.view.Home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

// Import DTO, Repository, dan Network yang sudah dibuat
import com.example.alp_vp.model.GameResponse
import com.example.alp_vp.container.RetrofitClient
import com.example.alp_vp.repository.CurrencyRepository

// --- VIEWMODEL (Otak Aplikasi) ---
class CalculatorViewModel : ViewModel() {

    // Inisialisasi Repository
    private val repository = CurrencyRepository(RetrofitClient.instance)

    // State untuk Data Game (Dari Backend)
    var gamesList = mutableStateListOf<GameResponse>()

    // State untuk Logic Hitung
    var currentRate by mutableStateOf(0.0)    // Harga per 1 unit (dari backend)
    var calculatedCost by mutableStateOf(0.0) // Hasil hitungan manual
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // Bundle standar untuk tabel harga (Disimpan di frontend untuk referensi tampilan)
    // Map ID Game ke List Bundle
    private val standardBundlesMap = mapOf(
        "Valorant" to listOf(475, 1000, 2050, 3650, 5350, 11000),
        "Genshin Impact" to listOf(60, 300, 980, 1980, 3280, 6480),
        "Roblox" to listOf(400, 800, 1700, 4500, 10000)
    )

    // Fungsi: Ambil List Game dari Backend
    fun fetchGames() {
        viewModelScope.launch {
            isLoading = true
            try {
                val games = repository.getGames()
                gamesList.clear()
                gamesList.addAll(games)
            } catch (e: Exception) {
                errorMessage = "Gagal ambil game: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // Fungsi: Ambil Rate Harga dari Backend
    fun fetchExchangeRate(gameId: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                // Panggil API getRates
                val rates = repository.getRates(gameId)

                // Ambil rate pertama (atau sesuaikan logika jika ada banyak currency)
                // Backend mengirim CurrencyRateResponse
                if (rates.isNotEmpty()) {
                    currentRate = rates[0].toIDR // Ambil field toIDR
                } else {
                    currentRate = 0.0
                }
            } catch (e: Exception) {
                currentRate = 0.0
                // Error silent agar user tidak terganggu popup, cukup harga jadi 0
            } finally {
                isLoading = false
            }
        }
    }

    // Fungsi: Hitung Manual (Di Frontend, menggunakan Rate yang sudah diambil)
    // Kita hitung di sini agar responsif tanpa loading setiap ketik
    fun calculateManual(amount: String) {
        val valAmount = amount.toIntOrNull() ?: 0
        calculatedCost = valAmount * currentRate
    }

    // Helper untuk UI ambil bundle berdasarkan nama game
    fun getBundlesForGame(gameName: String): List<Int> {
        return standardBundlesMap[gameName] ?: listOf(100, 500, 1000)
    }
}

// --- UI COMPOSABLE ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyCalculatorView(
    viewModel: CalculatorViewModel = viewModel()
) {
    // 1. Load Game saat pertama dibuka
    LaunchedEffect(Unit) {
        viewModel.fetchGames()
    }

    // State Lokal UI
    var selectedGameId by remember { mutableStateOf<Int?>(null) }
    var amountInput by remember { mutableStateOf("") }

    // Pilih game pertama otomatis jika list sudah termuat
    if (selectedGameId == null && viewModel.gamesList.isNotEmpty()) {
        selectedGameId = viewModel.gamesList[0].id
    }

    // Data Game Terpilih
    val selectedGameData = viewModel.gamesList.find { it.id == selectedGameId }

    // Jika game berubah, ambil rate baru
    LaunchedEffect(selectedGameId) {
        if (selectedGameId != null) {
            viewModel.fetchExchangeRate(selectedGameId!!)
            viewModel.calculatedCost = 0.0
            amountInput = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // [HEADER]
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Calculate, null, tint = Color(0xFF7E22CE), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Currency Calculator", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
            }
            Text("Convert in-game currency to real money", fontSize = 14.sp, color = Color(0xFF374151))
        }

        // Jika loading game
        if (viewModel.gamesList.isEmpty() && viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (selectedGameData != null) {
            // [GAME SELECTOR]
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                border = BorderStroke(2.dp, Color(0xFFD8B4FE)),
                shape = RoundedCornerShape(16.dp)
            ) {
                var expanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = selectedGameData.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Game") },
                        // Menggunakan IconUrl dari backend (jika null pakai icon default)
                        leadingIcon = { Text("🎮", fontSize = 18.sp) },
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().clickable { expanded = true },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD8B4FE),
                            unfocusedBorderColor = Color(0xFFD8B4FE)
                        )
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        viewModel.gamesList.forEach { game ->
                            DropdownMenuItem(
                                text = { Text(game.name) },
                                onClick = {
                                    selectedGameId = game.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // [CALCULATOR INPUT]
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                border = BorderStroke(2.dp, Color(0xFFD8B4FE)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Currency", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = {
                            amountInput = it
                            viewModel.calculateManual(it)
                        },
                        placeholder = { Text("Enter amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD8B4FE),
                            unfocusedBorderColor = Color(0xFFD8B4FE)
                        )
                    )

                    if (viewModel.calculatedCost > 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                            border = BorderStroke(1.dp, Color(0xFF4ADE80)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Real Cost", fontSize = 12.sp, color = Color.Gray)
                                    Text("Rp ${"%,.0f".format(viewModel.calculatedCost)}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF15803D))
                                }
                                Text("💰", fontSize = 24.sp)
                            }
                        }
                    }
                }
            }

            // [OFFICIAL PRICES TABLE]
            var isTableExpanded by remember { mutableStateOf(true) }
            val bundles = viewModel.getBundlesForGame(selectedGameData.name)

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                border = BorderStroke(2.dp, Color(0xFFD8B4FE)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isTableExpanded = !isTableExpanded }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AttachMoney, null, tint = Color(0xFF7E22CE))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Official Prices", fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                        }
                        Icon(
                            if (isTableExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            null, tint = Color.Gray
                        )
                    }

                    AnimatedVisibility(visible = isTableExpanded) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                            bundles.forEach { bundleAmount ->
                                val estimatedPrice = bundleAmount * viewModel.currentRate

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(Color(0xFFF3E8FF), RoundedCornerShape(8.dp))
                                        .border(1.dp, Color(0xFFE9D5FF), RoundedCornerShape(8.dp))
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFE9D5FF), RoundedCornerShape(6.dp))
                                                .padding(6.dp)
                                        ) {
                                            Text("💎", fontSize = 14.sp)
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text("$bundleAmount", fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Rp ${"%,.0f".format(estimatedPrice)}",
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyCalculatorPreview() {
    CurrencyCalculatorView()
}