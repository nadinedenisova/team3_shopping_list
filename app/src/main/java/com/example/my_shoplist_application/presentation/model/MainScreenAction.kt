package com.example.my_shoplist_application.presentation.model

sealed interface MainScreenAction {
    class ShowDeletingShoplistConfirmation(val isDialogDeleteVisible: Boolean) : MainScreenAction
}