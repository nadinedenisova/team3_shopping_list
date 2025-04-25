package com.example.my_shoplist_application.presentation.model

data class ShoplistScreenState(
    val currentState: CurrentState
) {

    enum class CurrentState {
        DEFAULT,
        NEW_LIST_STATE,
        EDIT_LIST_STATE,
        ADDING_INGRIDIENT_STATE
    }
}