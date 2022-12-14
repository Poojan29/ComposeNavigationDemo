package com.whiz.composenavigation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun FirstScreen(
    popToSecondScreen: () -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "On First screen")
        TextField(
            value = name,
            onValueChange = {
                newName -> name = newName
            }
        )
        Text(text = "Your name is ${name.text} right?")
        Button(onClick = popToSecondScreen ) {
            Text(text = "Navigate to second screen")
        }
    }
}