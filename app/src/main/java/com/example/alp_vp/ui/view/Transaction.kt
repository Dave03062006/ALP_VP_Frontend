package com.example.alp_vp.ui.view

import android.app.Dialog
import android.widget.Button
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
import androidx.compose.material.icons.filled.AccountBox
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.w3c.dom.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
){
    var selectedGame by remember { mutableStateOf("Valorant") }
    var selectedSpendType by remember { mutableStateOf("Single") }
    var selectedEvent by remember { mutableStateOf("None") }
    var amount by remember { mutableStateOf("") }
    var isAutoMode by remember { mutableStateOf(true) }
    var manualCost by remember { mutableStateOf("") }

    var gameExpanded by remember { mutableStateOf(false) }
    var spendExpanded by remember { mutableStateOf(false) }
    var eventExpanded by remember { mutableStateOf(false) }

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
                    Text("Select Game", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = gameExpanded,
                        onExpandedChange = { gameExpanded = !gameExpanded}
                    ) {
                        OutlinedTextField(
                            value = selectedGame,
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
                            listOf("Valorant", "Genshin Impact", "Honkai Star Rail", "League of Legends", "Apex Legends", "Roblox").forEach { game ->
                                DropdownMenuItem(
                                    text = { Text(game) },
                                    onClick = {
                                        selectedGame = game
                                        gameExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("How did you spend it?", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = spendExpanded,
                        onExpandedChange = { spendExpanded = !spendExpanded}
                    ) {
                        OutlinedTextField(
                            value = selectedSpendType,
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
                            listOf("Single", "Bundle", "Discount").forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        selectedSpendType = type
                                        spendExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Event / Season / Battle Pass (Optional)", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = eventExpanded,
                        onExpandedChange = { eventExpanded = !eventExpanded}
                    ) {
                        OutlinedTextField(
                            value = selectedEvent,
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
                            listOf("None", "Battle Pass", "Special Event", "Season Sale").forEach { event->
                                DropdownMenuItem(
                                    text = { Text(event)},
                                    onClick = {
                                        selectedEvent= event
                                        eventExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("VP (Valorant Points) Amount", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = amount,
                        onValueChange = {amount = it},
                        placeholder = { Text("Enter Amount")},
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                isAutoMode = true
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAutoMode) Color(0xFFD946EF) else Color(0xFFF3E8FF),
                                contentColor = if (isAutoMode) Color.White else Color(0xFF6B4FA0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Icon(Icons.Default.AccountBox, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Auto")
                        }
                        Button(
                            onClick = { isAutoMode = false},
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isAutoMode) Color(0xFFD946EF) else Color(0xFFF3E8FF),
                                contentColor = if (!isAutoMode) Color.White else Color(0xFF6B4FA0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Manual")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if(!isAutoMode){
                        Text("Enter IDR Amount", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = manualCost,
                            onValueChange = {manualCost = it},
                            placeholder = { Text("Enter Amount")},
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF3E8FF),
                                focusedContainerColor = Color(0xFFF3E8FF),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    Text("Calculated Cost", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text(
                        "Rp 0.00",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B4FA0)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD946EF)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Purchase", fontSize = 16.sp)
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
        onConfirm = {}
    )
}