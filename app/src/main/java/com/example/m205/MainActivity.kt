package com.example.m205

import android.app.AlarmManager
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MainScreen()
            }
        }
    }
}

data class Device(
    val name: String,
    val address: String,

)

@Composable
fun MainScreen(){
    val context = LocalContext.current
    var btAdapter = getBluetoothAdapter(context)
    val adapterExists by lazy { btAdapter!=null }
    var btEnabled by remember { mutableStateOf(btAdapter?.isEnabled ?: false) }
    val divices = remember { mutableStateListOf<Device>() }
    Text("adaper : $adapterExists")
    Text("enabled : $btEnabled")
    Button(onClick = {
        val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        context.startActivity(enableIntent)
        btEnabled = btAdapter?.isEnabled ?: false
    } ) {
        Text("enable BT")
    }
    Button(onClick = {
        divices.clear()
        val pairedDevices = btAdapter?.bondedDevices
        pairedDevices?.forEach {
            divices.add(Device(it.name, it.address))
        }
    } ) {
        Text("list paired devices")
    }
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (BluetoothDevice.ACTION_FOUND == intent.action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(
                        BluetoothDevice.EXTRA_DEVICE
                    )
                device?.let {
                    val bt = Device(
                        name = it.name ?: "Appareil inconnu",
                        address = it.address,
                    )
                    divices.add(bt)
                }
            }
        }
    }

    val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
    context.registerReceiver(receiver, filter)
    Button({
        divices.clear()
        btAdapter?.startDiscovery()

    }) {
        Text("discover devices")
    }
    LazyColumn() {
        items(divices){
            Text(text = "Name: ${it.name} - Address: ${it.address}")
        }
    }

}



fun getBluetoothAdapter(context: Context): BluetoothAdapter? {
    val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE)
                as BluetoothManager
    return bluetoothManager.adapter
}


class PasswordValidator {

    fun isValid(password: String): Boolean {
        if (password.length < 8) return false
        if (!password.any { it.isDigit() }) return false
        return true
    }
}



