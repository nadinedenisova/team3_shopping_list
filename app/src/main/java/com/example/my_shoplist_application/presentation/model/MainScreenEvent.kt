package com.example.my_shoplist_application.presentation.model

sealed interface MainScreenEvent {
    data object Default : MainScreenEvent
    data object OnBtnNewShopListClick : MainScreenEvent
    class OnDeleteShopListClick(val shoplistId: Int) : MainScreenEvent
    class OnRenameShopListClick(val shoplistId: Int, val shoplistName: String) : MainScreenEvent
    class OnDoubleShopListClick(val shoplistId: Int) : MainScreenEvent
    class OnShopListClick(val shoplistId: Int) : MainScreenEvent
    class OnTogglePinListClick(val shoplistId: Int) : MainScreenEvent
}