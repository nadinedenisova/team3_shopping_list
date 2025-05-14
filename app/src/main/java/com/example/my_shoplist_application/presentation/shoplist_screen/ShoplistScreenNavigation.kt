package com.example.my_shoplist_application.presentation.shoplist_screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.my_shoplist_application.presentation.main_screen.MainScreen

fun NavGraphBuilder.shoplistScreenNavigation(navController: NavHostController) {
    composable(
        "previewIngredients/{listId}",
        arguments = listOf(
            navArgument("listId") { type = NavType.IntType },
        ),
        enterTransition = {
            return@composable slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(350)
            )
        },
        exitTransition = {
            return@composable slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End, tween(350)
            )
        },
        popEnterTransition = {
            return@composable slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(350)
            )
        },
        popExitTransition = {
            return@composable slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End, tween(350)
            )
        }

    ) {backStackEntry ->
            val listId = backStackEntry.arguments?.getInt("listId") ?: return@composable
            ShoplistScreen(
                listId = listId,
                onBack = { navController.popBackStack() })
    }
}