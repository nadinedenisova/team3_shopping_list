package com.example.my_shoplist_application.presentation.model

sealed class ShoppingListEvent {
    data class NavigateToIngredients(val listId: Int) : ShoppingListEvent()
    data object HideDialog : ShoppingListEvent()
}
