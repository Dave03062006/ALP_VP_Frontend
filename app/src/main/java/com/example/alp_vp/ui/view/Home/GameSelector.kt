package com.example.alp_vp.ui.view.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alp_vp.data.dto.GameResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSelector(
    games: List<GameResponse> = emptyList(),
    selectedGame: GameResponse? = null,
    onGameSelected: (GameResponse?) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9D5FF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Select Game",
                fontSize = 14.sp,
                color = Color(0xFF6B4FA0)
            )
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedGame?.name ?: "All Games",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Transparent,
                        focusedBorderColor = Transparent
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All Games") },
                        onClick = {
                            onGameSelected(null)
                            expanded = false
                        }
                    )
                    games.forEach { game ->
                        DropdownMenuItem(
                            text = { Text(game.name) },
                            onClick = {
                                onGameSelected(game)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

