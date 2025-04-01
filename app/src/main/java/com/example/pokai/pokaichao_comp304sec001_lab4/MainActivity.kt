package com.example.pokai.pokaichao_comp304sec001_lab4

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokai.pokaichao_comp304sec001_lab4.Screens.CategoryScreen
import com.example.pokai.pokaichao_comp304sec001_lab4.Screens.HomeScreen
import com.example.pokai.pokaichao_comp304sec001_lab4.Screens.MapScreen
import com.example.pokai.pokaichao_comp304sec001_lab4.ui.theme.Pokaichao_COMP304Sec001_Lab4Theme
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pokaichao_COMP304Sec001_Lab4Theme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("category") { CategoryScreen() }
        composable("map") { MapScreen(navController) }
    }
}


