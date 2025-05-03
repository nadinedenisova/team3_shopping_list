package com.example.my_shoplist_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.my_shoplist_application.presentation.MainScreen
import com.google.gson.Gson


class MainActivity : ComponentActivity() {

    //  private val viewModel by viewModel <SecondScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gson = Gson()
        enableEdgeToEdge()
        setContent {
            MainScreen()
            //NavGraph(gson)
            //My_ShopList_ApplicationTheme {
            //    NavGraph(gson)
//                ShoppingListScreen()
            //PreviewShoppingListScreen()
        }
    }
    //  }
}
