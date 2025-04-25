package com.example.my_shoplist_application.presentation.model

sealed interface MainScreenState {
    data object Default : MainScreenState
}
