package com.whiz.composenavigation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ThirdScreen(
    name: String?,
    lastname: String?,
    popBackStack: () -> Unit,
    popBackToFirst: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,) {
        Text(text = "Welcome $name $lastname")
        Button(onClick = popBackToFirst) {
            Text(text = "Back to first screen")
        }
        Button(onClick = popBackStack) {
            Text(text = "Back to Second screen")
        }
    }
}