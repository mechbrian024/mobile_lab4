package com.example.pokai.pokaichao_comp304sec001_lab4

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
        composable("map/{locationName}") { backStackEntry ->
            val locationName = backStackEntry.arguments?.getString("locationName")
            locationName?.let { MapScreen(locationName, navController) }
        }
    }
}
//
//@RequiresApi(Build.VERSION_CODES.N)
//fun requestPermissions() {
//    val locationPermissionRequest = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        when {
//            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//                // Precise location access granted.
//            }
//            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//                // Only approximate location access granted.
//            }
//            else -> {
//                // No location access granted.
//            }
//        }
//    }
//
//    // Before you perform the actual permission request, check whether your app
//    // already has the permissions, and whether your app needs to show a permission
//    // rationale dialog. For more details, see Request permissions:
//    // https://developer.android.com/training/permissions/requesting#request-permission
//    locationPermissionRequest.launch(
//        arrayOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//    )
//}
//
//
