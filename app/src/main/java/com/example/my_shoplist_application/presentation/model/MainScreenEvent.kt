package com.example.my_shoplist_application.presentation.model

sealed interface MainScreenEvent {
    data object Default : MainScreenEvent
    data object OnBtnNewShopListClick : MainScreenEvent
    data object OnDeleteShopListClick : MainScreenEvent
    data object OnDismissDeleteShopListClick : MainScreenEvent
    data object OnCloseAddingWindow : MainScreenEvent
    class OnDeleteShopListConfirmClick(val shoplistId: Int) : MainScreenEvent
    class OnRenameShopListClick(val shoplistId: Int, val shoplistName: String) : MainScreenEvent
    class OnDoubleShopListClick(val shoplistId: Int) : MainScreenEvent
    class OnShopListClick(val shoplistId: Int) : MainScreenEvent
    class OnTogglePinListClick(val shoplistId: Int) : MainScreenEvent
    data class Add(val name: String) : MainScreenEvent
}
