package com.example.pokai.pokaichao_comp304sec001_lab4.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController){
    MyLocationCard(navController)
}

@Composable
fun MyLocationCard(navController: NavHostController)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("map") }
            .padding(top = 64.dp, start = 16.dp, end = 16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Where am I", style = MaterialTheme.typography.titleMedium)
        }
    }
}