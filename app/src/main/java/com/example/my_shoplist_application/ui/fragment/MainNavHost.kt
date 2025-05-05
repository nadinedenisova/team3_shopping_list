package com.example.my_shoplist_application.ui.fragment

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MainNavHost(
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "shoppingList") {
        composable("shoppingList") {
            ShoppingListScreen(
//                onListClick = {listId ->
//                    navController.navigate("previewIngredients/$listId")
//                }
            )
        }


        composable(
            "previewIngredients/{listId}",
            arguments = listOf(
                navArgument("listId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getInt("listId") ?: return@composable
        //    val listName = backStackEntry.arguments?.getString("listName") ?: ""

            IngredientsListScreen(
                listId = listId,
          //      listName = listName,
                onBack = { navController.popBackStack() })
        }
    }
}
