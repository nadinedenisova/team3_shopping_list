package com.example.my_shoplist_application.presentation.model

sealed interface ShoplistScreenState {
    data object NewList: ShoplistScreenState
    data object EditList: ShoplistScreenState
    data object AddingIngridient: ShoplistScreenState
}