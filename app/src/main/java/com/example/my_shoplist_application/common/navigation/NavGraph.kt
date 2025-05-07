package com.example.my_shoplist_application.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.my_shoplist_application.presentation.MainScreen
import com.example.my_shoplist_application.presentation.ShoplistScreen

//Первая верския навигации
//@Composable
//fun NavGraph(
//    gson: Gson,
//    startDestination: String = Routes.MainScreen.route,
//
//    ) {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = startDestination) {
//        composable(Routes.MainScreen.route) {
//            MainScreen(navController)
//        }
//        composable(
//            Routes.ShoplistScreen.route,
//            arguments = listOf(navArgument("shoplist") { type = NavType.StringType })
//        ) { navBackStackEntry ->
//            val shopListJson = navBackStackEntry.arguments?.getString("shoplist")
//            val shoplist = gson.fromJson(shopListJson, Shoplist::class.java)
//            ShoplistScreen(navController, shoplist)
//        }
//    }
//}

//Вторая версия
@Composable
fun NavGraph(
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "shoppingList") {
        composable("shoppingList") {
            /*ShoppingListScreen2*/ MainScreen(
                onListClick = {listId ->
                    navController.navigate("previewIngredients/$listId")
                }
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

            ShoplistScreen(
                listId = listId,
                //      listName = listName,
                onBack = { navController.popBackStack() })
        }
    }
}



