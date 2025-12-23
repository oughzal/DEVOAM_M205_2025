package com.example.m205

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.Calendar

class MainActivity : ComponentActivity(), EasyPermissions.PermissionCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    if(!shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION")){
                        requestPermissions(
                            this@MainActivity,
                            arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                            1001
                        )
                    }else{
                        openAppSettings(this@MainActivity)
                        return@Button
                    }



                }
                ) {
                    Text("GPS Permission")
                }

                Button(onClick = {

                    if(! EasyPermissions.hasPermissions(
                        this@MainActivity,
                            "android.permission.ACCESS_FINE_LOCATION"
                    )){
                        if(EasyPermissions.somePermissionPermanentlyDenied(
                                this@MainActivity,
                                mutableListOf("android.permission.ACCESS_FINE_LOCATION")
                            )){
                            AppSettingsDialog.Builder(this@MainActivity).build().show()
                            return@Button
                        }else {
                            EasyPermissions.requestPermissions(
                                this@MainActivity,
                                "We need location permission to show your location",
                                1002,
                                "android.permission.ACCESS_FINE_LOCATION"
                            )
                        }
                    }
                }) {
                    Text("EasyPermissions")
                }
            }
        }
    }
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(this@MainActivity, "Permissions Granted: $perms", Toast.LENGTH_SHORT).show()
    }
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(this@MainActivity, "Permissions Denied: $perms", Toast.LENGTH_SHORT).show()
    }
}

fun checkPermissions(context: Context,perm : String) : Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        perm
    ) == PackageManager.PERMISSION_GRANTED
}

fun requestPermissions(activity: ComponentActivity, perms: Array<String>, reqCode : Int) {
    activity.requestPermissions(
        perms,
        reqCode
    )
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = android.net.Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}



