package com.example.my_shoplist_application.common.navigation

sealed class Routes(val route: String) {
    data object MainScreen : Routes("main_screen")
    data object ShoplistScreen : Routes("shoplist_screen/{shoplist}")
}
