package com.whiz.composenavigation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.whiz.composenavigation.screens.FirstScreen
import com.whiz.composenavigation.screens.SecondScreen
import com.whiz.composenavigation.screens.ThirdScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "first/{name}"
    ) {
        addFirstScreen(navController, this)

        addSecondScreen(navController, this)

        addThirdScreen(navController, this)
    }
}

fun addFirstScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable("first/{name}", arguments = listOf(
        navArgument("name") {
            type = NavType.StringType
        }
    )) {
        FirstScreen(popToSecondScreen = { navController.navigate("second/{name}") })
    }
}

fun addSecondScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable("second/{name}",
        arguments = listOf(
            navArgument("lastname") {
                type = NavType.StringType
            },
            navArgument("name") {
                type = NavType.StringType
            }

        )) {
        val args = it.arguments
        val name = args?.getString("name")
        name?.let { it1 -> Log.d("TAG", it1) }
        SecondScreen(
            name = name,
            popBackStack = { navController.popBackStack() },
            popToThirdScreen = { navController.navigate("third/{name}/{lastname}") })
    }
}

fun addThirdScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable("third") {
        val args = it.arguments
        val name = args?.getString("name")
        val lastname = args?.getString("lastname")
        ThirdScreen(
            name = name,
            lastname = lastname,
            popBackStack = { navController.popBackStack() },
            popBackToFirst = {
                navController.navigate(
                    "first"
                )
            })
    }
}

