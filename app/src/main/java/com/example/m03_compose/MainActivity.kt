package com.example.m03_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainScreen()
            }


        }
    }
}


@Composable
fun MainScreen() {
    var nb1 by remember { mutableStateOf(5) }
    var nb2 by remember { mutableStateOf(2) }
    var result by remember { mutableStateOf(7) }
    var hideResult by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        OutlinedTextField(
            value = nb1.toString(),
            onValueChange = { nb1 = it.toIntOrNull() ?: 0 },
            label = { Text("Nombre 1") },
            modifier = Modifier.testTag("nb1")
        )
        OutlinedTextField(
            value = nb2.toString(),
            onValueChange = { nb2 = it.toIntOrNull() ?: 0 },
            label = { Text("Nombre 2") },
            modifier = Modifier.testTag("nb2")
        )
        Button(
            onClick = { result = nb1 + nb2 },
            modifier = Modifier
                .padding(top = 16.dp)
                .testTag("btnAdd")
        ) {
            Text("Additionner")
        }
        Switch(
            checked = hideResult,
            onCheckedChange = { hideResult = it },
            modifier = Modifier
                .padding(top = 16.dp)
                .testTag("chkHideResult")
        )

        if (!hideResult){
            Text(
                text = "$result",
                modifier = Modifier
                    .padding(top = 16.dp)
                    .testTag("result")
            )
        }

    }

}


