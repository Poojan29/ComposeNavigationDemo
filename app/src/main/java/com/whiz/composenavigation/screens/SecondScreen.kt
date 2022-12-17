package com.whiz.composenavigation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SecondScreen(
    popBackStack: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = { popBackStack.invoke() }) {
            Text(text = "Back to first")
        }
    }

}