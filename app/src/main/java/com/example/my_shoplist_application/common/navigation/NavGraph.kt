package com.example.my_shoplist_application.common.navigation

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




