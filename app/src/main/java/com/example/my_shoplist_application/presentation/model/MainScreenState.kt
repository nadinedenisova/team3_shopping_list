package com.example.my_shoplist_application.presentation.model

import com.example.my_shoplist_application.domain.models.Shoplist

sealed interface MainScreenState {
    data object Default : MainScreenState
    data class Shoplists(val shoplists: List<Shoplist>) : MainScreenState
}
