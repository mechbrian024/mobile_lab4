package com.example.pokai.pokaichao_comp304sec001_lab4

//import android.location.LocationRequest
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pokai.pokaichao_comp304sec001_lab4.Screens.CategoryScreen
import com.example.pokai.pokaichao_comp304sec001_lab4.Screens.HomeScreen
import com.example.pokai.pokaichao_comp304sec001_lab4.Screens.MapScreen
import com.example.pokai.pokaichao_comp304sec001_lab4.Screens.MapScreen2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // fused location is used to get the current location of the user
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize location state
        val userLocation = mutableStateOf(LatLng(1.35, 103.87))

        // Try to set last known location before UI loads
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                userLocation.value = LatLng(it.latitude, it.longitude)
            }
        }

        // define location callback
        // location callback is used to update the location state when the location changes
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach { location ->
// Update UI with location data
                    userLocation.value = LatLng(location.latitude, location.longitude)
// Update map or UI
                }
            }
        }

// Start location updates
        startLocationUpdates()

        enableEdgeToEdge()
        setContent {
//            Pokaichao_COMP304Sec001_Lab4Theme {
//                val navController = rememberNavController()
//                AppNavigation(navController)
//            }

            MapScreen2(fusedLocationClient, userLocation)
        }
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000
        ).apply {
            setMinUpdateIntervalMillis(500)
        }.build()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
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


