package com.example.my_shoplist_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import com.example.my_shoplist_application.ui.fragment.PreviewTodoScreen
import com.example.my_shoplist_application.ui.fragment.ShoppingListScreen
import com.example.my_shoplist_application.ui.theme.My_ShopList_ApplicationTheme
import com.example.my_shoplist_application.ui.viewmodel.ShoppingListViewModel
import kotlin.getValue


class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ShoppingListViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            My_ShopList_ApplicationTheme {
                //
                ShoppingListScreen(viewModel = viewModel)
            }
        }
    }
}