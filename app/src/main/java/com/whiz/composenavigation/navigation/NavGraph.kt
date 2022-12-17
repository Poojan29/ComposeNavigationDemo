package com.whiz.composenavigation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.whiz.composenavigation.screens.FirstScreen
import com.whiz.composenavigation.screens.SecondScreen
import com.whiz.composenavigation.viewmodel.MainViewModel

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "first"
    ) {
        addFirstScreen(navController, this)

        addSecondScreen(navController, this)
    }
}

fun addFirstScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable("first") {
        FirstScreen(
            popToSecondScreen = { navController.navigate("second") },
            viewModel = MainViewModel()
        )
    }
}


fun addSecondScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable("second") {
        SecondScreen(popBackStack = { navController.popBackStack() })
    }
}

