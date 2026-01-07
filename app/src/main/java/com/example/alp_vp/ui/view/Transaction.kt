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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.alp_vp.data.repository.EventRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDialog(
    profileId: Int,
    onDismiss: () -> Unit,
    onConfirm: (CreateTransactionRequest) -> Unit,
    eventRepository: EventRepository
){
    var selectedGameId by remember { mutableStateOf(1) }
    var selectedTransactionTypeId by remember { mutableStateOf(1) }
    var selectedEventId by remember { mutableStateOf<Int?>(null) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var gameExpanded by remember { mutableStateOf(false) }
    var spendExpanded by remember { mutableStateOf(false) }
    var eventExpanded by remember { mutableStateOf(false) }

    // Dynamic events list based on selected game
    var availableEvents by remember { mutableStateOf<List<Pair<String, Int?>>>(listOf("None" to null)) }
    var isLoadingEvents by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

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
    val transactionTypes = listOf(
        "Single Purchase" to 1,
        "Bundle" to 2,
        "Discount" to 3
    )

    var selectedGameName by remember { mutableStateOf(games[0].first) }
    var selectedTypeName by remember { mutableStateOf(transactionTypes[0].first) }
    var selectedEventName by remember { mutableStateOf("None") }

    // Load events when game changes
    LaunchedEffect(selectedGameId) {
        isLoadingEvents = true
        try {
            val events = eventRepository.getEventsByGame(selectedGameId)
            availableEvents = listOf("None" to null) + events.map { it.eventName to it.id }
            // Reset event selection to "None" when game changes
            selectedEventName = "None"
            selectedEventId = null
        } catch (e: Exception) {
            println("Error loading events: ${e.message}")
            availableEvents = listOf("None" to null)
        } finally {
            isLoadingEvents = false
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clickable(enabled = false) { },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBFE))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Header with better layout
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFF5F0FF),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color(0xFFD946EF),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Column {
                                Text(
                                    "Quick Add Purchase",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1C1B1F)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    "Track your in-game purchases and earn points",
                                    fontSize = 11.sp,
                                    color = Color(0xFF6B4FA0)
                                )
                            }
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF6B4FA0)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Game Selector
                    Text(
                        "Select Game",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF49454F)
                    )
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
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = Color(0xFFD946EF)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    if (gameExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color(0xFF6B4FA0)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                unfocusedBorderColor = Color(0xFFE0D4F7),
                                focusedBorderColor = Color(0xFFD946EF)
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
                    Text(
                        "How did you spend it?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF49454F)
                    )
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
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = Color(0xFFD946EF)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    if (spendExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color(0xFF6B4FA0)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                unfocusedBorderColor = Color(0xFFE0D4F7),
                                focusedBorderColor = Color(0xFFD946EF)
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
                    Text(
                        "Event / Season (Optional)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF49454F)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = eventExpanded,
                        onExpandedChange = { eventExpanded = !eventExpanded}
                    ) {
                        OutlinedTextField(
                            value = if (isLoadingEvents) "Loading..." else selectedEventName,
                            onValueChange = {},
                            readOnly = true,
                            enabled = !isLoadingEvents,
                            trailingIcon = {
                                Icon(
                                    if(eventExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color(0xFF6B4FA0)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                unfocusedBorderColor = Color(0xFFE0D4F7),
                                focusedBorderColor = Color(0xFFD946EF),
                                disabledContainerColor = Color(0xFFF5F5F5),
                                disabledBorderColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = eventExpanded,
                            onDismissRequest = { eventExpanded = false}
                        ) {
                            availableEvents.forEach { (name, id) ->
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

                    // Amount Input with highlight
                    Text(
                        "Amount Spent (IDR)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF49454F)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = amount,
                        onValueChange = {
                            // Only allow numbers
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                amount = it
                            }
                        },
                        placeholder = {
                            Text(
                                "e.g., 50000",
                                color = Color(0xFF9E9E9E),
                                fontSize = 14.sp
                            )
                        },
                        prefix = {
                            Text(
                                "Rp ${"%,.0f".format(amount.toDoubleOrNull() ?: 0.0)}",
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFD946EF)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFFFFBFE),
                            focusedContainerColor = Color(0xFFFFF8FC),
                            unfocusedBorderColor = Color(0xFFE0D4F7),
                            focusedBorderColor = Color(0xFFD946EF),
                            cursorColor = Color(0xFFD946EF)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Description (Optional)
                    Text(
                        "Description (Optional)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF49454F)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = {description = it},
                        placeholder = {
                            Text(
                                "Add notes about this purchase...",
                                color = Color(0xFF9E9E9E),
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFE0D4F7),
                            focusedBorderColor = Color(0xFFD946EF),
                            cursorColor = Color(0xFFD946EF)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Info card about points
                    if (amount.toDoubleOrNull() != null && amount.toDoubleOrNull()!! > 0) {
                        val estimatedPoints = (amount.toDoubleOrNull()!! * 0.01).toInt() // Basic calculation
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFEEFC)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = Color(0xFFD946EF),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Column {
                                    Text(
                                        "You'll earn approximately",
                                        fontSize = 11.sp,
                                        color = Color(0xFF6B4FA0)
                                    )
                                    Text(
                                        "$estimatedPoints points",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD946EF)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Submit Button with gradient effect
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD946EF),
                            disabledContainerColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = amount.toDoubleOrNull() != null && amount.toDoubleOrNull()!! > 0
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Add Transaction",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun TransactionDialogPreview() {
    // Preview won't work without providing eventRepository
    // TransactionDialog(onDismiss = {}, onConfirm = {}, profileId = 1, eventRepository = ...)
}