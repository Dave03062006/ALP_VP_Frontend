package com.example.alp_vp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.alp_vp.data.dto.transaction.CreateTransactionRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDialog(
    profileId: Int,
    onDismiss: () -> Unit,
    onConfirm: (CreateTransactionRequest) -> Unit
){
    var selectedGameId by remember { mutableStateOf(1) }
    var selectedTransactionTypeId by remember { mutableStateOf(1) }
    var selectedEventId by remember { mutableStateOf<Int?>(null) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var gameExpanded by remember { mutableStateOf(false) }
    var spendExpanded by remember { mutableStateOf(false) }
    var eventExpanded by remember { mutableStateOf(false) }

    // Updated to match the seeded games and transaction types
    val games = listOf(
        "Valorant" to 1,
        "Genshin Impact" to 2,
        "Mobile Legends" to 3,
        "Roblox" to 4,
        "PUBG Mobile" to 5,
        "Free Fire" to 6,
        "Honkai: Star Rail" to 7
    )
    // Updated to match the seeded transaction types
    val transactionTypes = listOf(
        "Single Purchase" to 1,
        "Bundle" to 2,
        "Discount" to 3
    )
    val events = listOf(
        "None" to null,
        "Battle Pass" to 1,
        "Special Event" to 2,
        "Season Sale" to 3
    )

    var selectedGameName by remember { mutableStateOf(games[0].first) }
    var selectedTypeName by remember { mutableStateOf(transactionTypes[0].first) }
    var selectedEventName by remember { mutableStateOf(events[0].first) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clickable(enabled = false) { },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Quick Add Purchase",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }
                    }
                    Text(
                        "Track your gaming expense and earn points",
                        fontSize = 12.sp,
                        color = Color(0xFF6B4FA0)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Game Selector
                    Text("Select Game", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = gameExpanded,
                        onExpandedChange = { gameExpanded = !gameExpanded}
                    ) {
                        OutlinedTextField(
                            value = selectedGameName,
                            onValueChange = {},
                            readOnly = true,
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = Color(0xFF9C6FDE))
                            },
                            trailingIcon = {
                                Icon(
                                    if (gameExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF3E8FF),
                                focusedContainerColor = Color(0xFFF3E8FF),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = gameExpanded,
                            onDismissRequest = {gameExpanded = false}
                        ) {
                            games.forEach { (name, id) ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        selectedGameName = name
                                        selectedGameId = id
                                        gameExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Transaction Type
                    Text("How did you spend it?", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = spendExpanded,
                        onExpandedChange = { spendExpanded = !spendExpanded}
                    ) {
                        OutlinedTextField(
                            value = selectedTypeName,
                            onValueChange = {},
                            readOnly = true,
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, tint = Color(0xFF9C6FDE))
                            },
                            trailingIcon = {
                                Icon(
                                    if (spendExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF3E8FF),
                                focusedContainerColor = Color(0xFFF3E8FF),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = spendExpanded,
                            onDismissRequest = { spendExpanded = false}
                        ) {
                            transactionTypes.forEach { (name, id) ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        selectedTypeName = name
                                        selectedTransactionTypeId = id
                                        spendExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Event Selector
                    Text("Event / Season (Optional)", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = eventExpanded,
                        onExpandedChange = { eventExpanded = !eventExpanded}
                    ) {
                        OutlinedTextField(
                            value = selectedEventName,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    if(eventExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF3E8FF),
                                focusedContainerColor = Color(0xFFF3E8FF),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = eventExpanded,
                            onDismissRequest = { eventExpanded = false}
                        ) {
                            events.forEach { (name, id) ->
                                DropdownMenuItem(
                                    text = { Text(name)},
                                    onClick = {
                                        selectedEventName = name
                                        selectedEventId = id
                                        eventExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Amount Input
                    Text("Amount Spent (IDR)", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = amount,
                        onValueChange = {amount = it},
                        placeholder = { Text("Enter amount in IDR")},
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF3E8FF),
                            focusedContainerColor = Color(0xFFF3E8FF),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Description (Optional)
                    Text("Description (Optional)", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = {description = it},
                        placeholder = { Text("Add notes...")},
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF3E8FF),
                            focusedContainerColor = Color(0xFFF3E8FF),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            val amountDouble = amount.toDoubleOrNull()
                            if (amountDouble != null && amountDouble > 0) {
                                val request = CreateTransactionRequest(
                                    gameId = selectedGameId,
                                    transactionTypeId = selectedTransactionTypeId,
                                    amount = amountDouble,
                                    eventId = selectedEventId
                                )
                                onConfirm(request)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD946EF)),
                        shape = RoundedCornerShape(12.dp),
                        enabled = amount.toDoubleOrNull() != null && amount.toDoubleOrNull()!! > 0
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Transaction", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun TransactionDialogPreview() {
    TransactionDialog(
        onDismiss = {},
        onConfirm = {},
        profileId = 1
    )
}