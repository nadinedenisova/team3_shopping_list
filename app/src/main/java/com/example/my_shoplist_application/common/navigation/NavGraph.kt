package com.example.my_shoplist_application.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.my_shoplist_application.domain.models.Shoplist
import com.example.my_shoplist_application.presentation.MainScreen
import com.example.my_shoplist_application.presentation.ShoplistScreen
import com.google.gson.Gson

@Composable
fun NavGraph(
    gson: Gson,
    startDestination: String = Routes.MainScreen.route,

    ) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.MainScreen.route) {
            MainScreen(/*navController*/)
        }
        composable(
            Routes.ShoplistScreen.route,
            arguments = listOf(navArgument("shoplist") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val shopListJson = navBackStackEntry.arguments?.getString("shoplist")
            val shoplist = gson.fromJson(shopListJson, Shoplist::class.java)
            ShoplistScreen(navController, shoplist)
        }
    }
}
