package com.example.my_shoplist_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.my_shoplist_application.ui.fragment.PreviewShoppingListScreen
import com.example.my_shoplist_application.ui.theme.My_ShopList_ApplicationTheme


class MainActivity : ComponentActivity() {

  //  private val viewModel by viewModel <SecondScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            My_ShopList_ApplicationTheme {
//                ShoppingListScreen()
                PreviewShoppingListScreen()
            }
        }
    }
}
