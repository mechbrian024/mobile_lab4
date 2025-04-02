package com.example.pokai.pokaichao_comp304sec001_lab4.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pokai.pokaichao_comp304sec001_lab4.data.DataSource

@Composable
fun HomeScreen(navController: NavHostController){

    Column {
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
                Text("My Location", style = MaterialTheme.typography.titleMedium)
            }
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            itemsIndexed(items = DataSource.restaurantLocations.keys.toList()) { index, locationName ->
                MyLocationCard(location = locationName, navController = navController)
            }
        }
    }
}

@Composable
fun MyLocationCard(location: String, navController: NavHostController)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("map/${location}") }
            .padding(top = 64.dp, start = 16.dp, end = 16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(location, style = MaterialTheme.typography.titleMedium)
        }
    }
}