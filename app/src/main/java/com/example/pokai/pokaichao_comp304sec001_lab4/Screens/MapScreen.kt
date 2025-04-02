package com.example.pokai.pokaichao_comp304sec001_lab4.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pokai.pokaichao_comp304sec001_lab4.PermissionRationaleDialog
import com.example.pokai.pokaichao_comp304sec001_lab4.PermissionRequestButton
import com.example.pokai.pokaichao_comp304sec001_lab4.RationaleState
import com.example.pokai.pokaichao_comp304sec001_lab4.data.DataSource
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "PermissionLaunchedDuringComposition",
    "CoroutineCreationDuringComposition"
)
@Composable
fun MapScreen(selectedItem: String, navController: NavHostController) {
    val context = LocalContext.current

    val coarseLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val fineLocationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val backgroundLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    val locationManager = remember {
        LocationManager(
            context,
            LocationServices.getFusedLocationProviderClient(context)
        )
    }
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    val coroutineScope = rememberCoroutineScope()

    if (!fineLocationPermissionState.allPermissionsGranted || fineLocationPermissionState.shouldShowRationale) {
        fineLocationPermissionState.launchMultiplePermissionRequest()
    } else {
        coroutineScope.launch {
            currentLocation = locationManager.getLocation()
        }
    }

    var currentLocationCoords = currentLocation?.let { LatLng(it.latitude,it.longitude) }

    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
    }

    // Show rationale dialog when needed
    rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }

    PermissionRequestButton(
        isGranted = coarseLocationPermissionState.status.isGranted,
        title = "Approximate location access",
    ) {
        if (coarseLocationPermissionState.status.shouldShowRationale) {
            rationaleState = RationaleState(
                "Request approximate location access",
                "In order to use this feature please grant access by accepting " + "the location permission dialog." + "\n\nWould you like to continue?",
            ) { proceed ->
                if (proceed) {
                    coarseLocationPermissionState.launchPermissionRequest()
                }
                rationaleState = null
            }
        } else {
            coarseLocationPermissionState.launchPermissionRequest()
        }
    }

    PermissionRequestButton(
        isGranted = fineLocationPermissionState.allPermissionsGranted,
        title = "Precise location access",
    ) {
        if (fineLocationPermissionState.shouldShowRationale) {
            rationaleState = RationaleState(
                "Request Precise Location",
                "In order to use this feature please grant access by accepting " + "the location permission dialog." + "\n\nWould you like to continue?",
            ) { proceed ->
                if (proceed) {
                    fineLocationPermissionState.launchMultiplePermissionRequest()
                }
                rationaleState = null
            }
        } else {
            fineLocationPermissionState.launchMultiplePermissionRequest()
        }
    }

    PermissionRequestButton(
        isGranted = backgroundLocationPermissionState.status.isGranted,
        title = "Background location access",
    ) {
        if (backgroundLocationPermissionState.status.isGranted || fineLocationPermissionState.allPermissionsGranted) {
            if (backgroundLocationPermissionState.status.shouldShowRationale) {
                rationaleState = RationaleState(
                    "Request background location",
                    "In order to use this feature please grant access by accepting " + "the background location permission dialog." + "\n\nWould you like to continue?",
                ) { proceed ->
                    if (proceed) {
                        backgroundLocationPermissionState.launchPermissionRequest()
                    }
                    rationaleState = null
                }
            } else {
                backgroundLocationPermissionState.launchPermissionRequest()
            }
        } else {
            Toast.makeText(
                context,
                "Please grant either Approximate location access permission or Fine" + "location access permission",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    // Default to a fallback location if the item is unknown
    val location = DataSource.restaurantLocations[selectedItem] ?: LatLng(0.0, 0.0)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 12f)  // Adjust zoom level as needed
    }

//    new code
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Map for $selectedItem") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = com.google.maps.android.compose.MarkerState(position = location),
                title = selectedItem,
                snippet = "Location of $selectedItem"
            )
            currentLocationCoords?.let { MarkerState(position = it) }?.let {
                Marker(
                    state = it,
                    title = "Your Location"
                )
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun MapScreen_old(navController: NavHostController) {
    val context = LocalContext.current

    val locationManager = remember {
        com.example.pokai.pokaichao_comp304sec001_lab4.Screens.LocationManager(
            context,
            LocationServices.getFusedLocationProviderClient(context)
        )
    }

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    var location by remember { mutableStateOf<Location?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        location?.let {
            Text(
                text = "Latitude: ${it.latitude}, Longitude: ${it.longitude}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = {
                if (!locationPermissions.allPermissionsGranted || locationPermissions.shouldShowRationale) {
                    locationPermissions.launchMultiplePermissionRequest()
                } else {
                    coroutineScope.launch {
                        location = locationManager.getLocation()
                    }
                }
            }
        ) {
            Text(text = "Get my location")
        }
    }
}

class LocationManager(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

    suspend fun getLocation(): Location? {

        val hasGrantedFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasGrantedCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled && !(hasGrantedCoarseLocationPermission || hasGrantedFineLocationPermission)) {
            return null
        }

        return suspendCancellableCoroutine { cont ->

            fusedLocationProviderClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(result)
                    } else {
                        cont.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }

                addOnSuccessListener {
                    cont.resume(result)
                }

                addOnFailureListener {
                    cont.resume(null)
                }

                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }
}