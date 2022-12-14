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
fun SecondScreen(
    popBackStack: () -> Unit,
    popToThirdScreen: () -> Unit,
    name: String?
) {
    var lastName by remember {
        mutableStateOf(TextFieldValue())
    }
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,) {
        Text(text = "Welcome $name \nyou are on second screen.")
        TextField(value = lastName, onValueChange = {
            newLastName -> lastName = newLastName
        })
        Text(text = "Can we call you by $name ${lastName.text}")
        Button(onClick = popToThirdScreen) {
            Text(text = "Pop to third screen")
        }
        Button(onClick = popBackStack) {
            Text(text = "Pop to first screen")
        }
    }
}