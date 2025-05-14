package com.example.my_shoplist_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.my_shoplist_application.presentation.main_screen.mainScreenNavigation
import com.example.my_shoplist_application.presentation.shoplist_screen.shoplistScreenNavigation
import com.example.my_shoplist_application.presentation.ui.theme.My_ShopList_ApplicationTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gson = Gson()
        enableEdgeToEdge()
        setContent {
            My_ShopList_ApplicationTheme {
                val navController = rememberNavController()
                NavGraph(navController)

            }
        }
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
) {
    // val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "shoppingList",
    ) {
        mainScreenNavigation(navController)
        shoplistScreenNavigation(navController)
    }

}

//        composable(
//            "previewIngredients/{listId}",
//            arguments = listOf(
//                navArgument("listId") { type = NavType.IntType },
//            )
//        ) { backStackEntry ->
//            val listId = backStackEntry.arguments?.getInt("listId") ?: return@composable
//            ShoplistScreen(
//                listId = listId,
//                onBack = { navController.popBackStack() })
//        }
//    }
//}
