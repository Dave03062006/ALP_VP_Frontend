package com.example.alp_vp.ui.view.gacha

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alp_vp.ui.data.gameList
import com.example.alp_vp.ui.viewmodel.GachaViewModel

@Composable
fun GachaScreen(viewModel: GachaViewModel) {

    val state = viewModel.uiState.collectAsState()
    val selectedGameId = viewModel.selectedGameId.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔹 HEADER
        Text(
            text = "Gacha Simulator",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Test your luck with 10 points per pull",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 POINT CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF3E9FF)
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF9C6BFF)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Your Points")
                }

                Text(
                    text = state.value.points.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF9C6BFF)
                )
            }
        }


        // 🔹 SELECT GAME
        Text(
            text = "Select Game",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(gameList) { game ->
                GameCard(
                    game = game,
                    isSelected = selectedGameId.value == game.id,
                    onClick = { viewModel.selectGame(game.id) }
                )
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 RESULT CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F2FF) // biru muda
            )

        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (state.value.results.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF9C6BFF),
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "No gacha result yet",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Try your luck!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }


            } else {
                    val last = state.value.results.last()
                    Text(
                        text = "RARE",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = last.itemName,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 ACTION BUTTONS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.48f)
                    .height(60.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFF9ECF)
                ),
                onClick = { }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Single Pull")
                    Text("10 points", style = MaterialTheme.typography.bodySmall)
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFC285)
                ),
                onClick = { }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("10x Pull")
                    Text("100 points", style = MaterialTheme.typography.bodySmall)
                }
            }
        }



        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 DROP RATE
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF4F4F4)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Drop Rates", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                DropRateItem("Mythic", "0.5%", Color.Red)
                DropRateItem("Legendary", "2%", Color(0xFFFF9800))
                DropRateItem("Epic", "10%", Color(0xFF9C27B0))
                DropRateItem("Rare", "25%", Color(0xFF3F51B5))
                DropRateItem("Uncommon", "30%", Color(0xFF4CAF50))
                DropRateItem("Common", "30.5%", Color.Gray)
            }
        }

    }
}


@Composable
fun DropRateItem(name: String, rate: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name)
        Text(rate)
    }
}
@Composable
fun GachaButton(
    text: String,
    subtitle: String,
    gradient: List<Color>,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(64.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Brush.horizontalGradient(gradient))
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(text, color = Color.White)
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
@Composable
fun DropRateItem(name: String, rate: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, color = color)
        Text(rate)
    }
}





