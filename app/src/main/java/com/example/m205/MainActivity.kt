package com.example.m205

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var secondes by remember { mutableStateOf(60)}
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = "$secondes",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = {
                        secondes = it.toIntOrNull() ?: 0
                    },
                    label = { Text("Alarm in secondes") }
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            val time = System.currentTimeMillis() + secondes * 1000
                            val pendingIntent = buildPendingIntent(applicationContext, 0)

                            alarmManager.setAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                time,
                                pendingIntent
                            )

                        }
                    ) {
                        Text("set Alarm")
                    }
                    Button(
                        onClick = {
                            val pendingIntent = buildPendingIntent(applicationContext, 0)
                            alarmManager.cancel( pendingIntent)
                        }
                    ) {
                        Text("cancel Alarm")
                    }
                    Button(onClick = {
                        val prayers = calculator.calculate(LocalDate.now())
                        Toast.makeText(this@MainActivity, prayers.toString(), Toast.LENGTH_LONG).show()

                    }) {
                        Text("Get Prayer Times")
                    }
                }
            }
        }
    }
}

fun buildPendingIntent(context: Context, id : Int ): PendingIntent {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        action = "ACTION_ALARM_RING"
        putExtra("ALARM_ID", id)
    }

    return PendingIntent.getBroadcast(
        context,
        id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )


}





