package com.example.my_shoplist_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.my_shoplist_application.common.navigation.NavGraph
import com.example.my_shoplist_application.ui.theme.My_ShopList_ApplicationTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {

    //  private val viewModel by viewModel <SecondScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gson = Gson()
        enableEdgeToEdge()
        setContent {

            //NavGraph(gson)

            My_ShopList_ApplicationTheme {
                // MainScreen()
                NavGraph(gson)
//                ShoppingListScreen()
                //PreviewShoppingListScreen()
            }
        }
    }
    //  }
}
