package com.whiz.composenavigation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.whiz.composenavigation.screens.FirstScreen
import com.whiz.composenavigation.screens.SecondScreen
import com.whiz.composenavigation.viewmodel.MainViewModel

@Composable
fun NavGraph(navController: NavHostController, context: Context) {

    NavHost(
        navController = navController,
        startDestination = "first"
    ) {
        addFirstScreen(navController, this, context)

        addSecondScreen(navController, this)
    }
}

fun addFirstScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder, context: Context) {
    navGraphBuilder.composable("first") {
        FirstScreen(
            popToSecondScreen = { navController.navigate("second") },
            viewModel = MainViewModel(context = context),
            context = context
        )
    }
}


fun addSecondScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable("second") {
        SecondScreen(popBackStack = { navController.popBackStack() })
    }
}

