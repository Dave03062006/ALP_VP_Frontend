package com.example.alp_vp.ui.view.gacha

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alp_vp.ui.viewmodel.GachaViewModel

@Composable
fun GachaScreen(
    viewModel: GachaViewModel
) {
    // Ambil state dari ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // HEADER
        Text(
            text = "Gacha Simulator",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // POINTS
        Text(
            text = "Your Points: ${uiState.points}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))


        if (uiState.results.isNotEmpty()) {
            Text("Results:")
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.results) { result ->
                    GachaResultCard(
                        result = result
                    )
                }
            }
        } else {
            Text("No gacha result yet")
        }


        Spacer(modifier = Modifier.height(32.dp))

        // BUTTON
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = {
                viewModel.singlePull()
            }) {
                Text("Single Pull")
            }

            Button(onClick = {
                viewModel.tenPull()
            }) {
                Text("10x Pull")
            }
        }
    }
}


