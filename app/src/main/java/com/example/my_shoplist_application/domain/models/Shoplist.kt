package com.example.my_shoplist_application.domain.models

data class Shoplist(
    val id: Int,
    val shoplistName: String,
    val ingredientsList: MutableList<Ingredients>,
    var isPinned: Boolean = false
)
