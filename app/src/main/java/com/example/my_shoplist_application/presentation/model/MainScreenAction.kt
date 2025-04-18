package com.example.my_shoplist_application.presentation.model

sealed interface MainScreenAction {
    data object ShowDeletingShoplistConfirmation : MainScreenAction
}