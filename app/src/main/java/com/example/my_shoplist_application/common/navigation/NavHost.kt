package com.example.my_shoplist_application.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.my_shoplist_application.domain.models.Shoplist
import com.example.my_shoplist_application.presentation.MainScreen
import com.example.my_shoplist_application.presentation.MainScreenViewModel
import com.example.my_shoplist_application.presentation.ShoplistScreen
import com.example.my_shoplist_application.presentation.ShoplistScreenViewModel
import com.google.gson.Gson

@Composable
fun NavGraph(
    startDestination: String = Routes.MainScreen.route,
    mainScreenViewModel: MainScreenViewModel,
    shoplistScreenViewModel: ShoplistScreenViewModel,
    gson: Gson
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.MainScreen.route) {
            MainScreen(navController, mainScreenViewModel)
        }
        composable(
            Routes.ShoplistScreen.route,
            arguments = listOf(navArgument("shoplist") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val shopListJson = navBackStackEntry.arguments?.getString("shoplist")
            val shoplist = gson.fromJson(shopListJson, Shoplist::class.java)
            ShoplistScreen(navController, shoplistScreenViewModel, shoplist)
        }
    }
}
