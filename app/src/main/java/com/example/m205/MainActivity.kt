package com.example.m205

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.room.util.copy
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                currentLocationMap()
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun getLocation(context: Context,onSucces :(LatLng)-> Unit){

    val location = LocationServices.getFusedLocationProviderClient(context)

    location.lastLocation
        .addOnSuccessListener { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                onSucces(LatLng(location.latitude, location.longitude))
            }
        }
}
@Composable
fun currentLocationMap(){
    val context = LocalContext.current
    var userLocation by remember{ mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(Unit) {
        getLocation(context) { latLng ->
            userLocation = latLng
            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 20f)
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        userLocation?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Ma position"
            )
        }
    }
}


