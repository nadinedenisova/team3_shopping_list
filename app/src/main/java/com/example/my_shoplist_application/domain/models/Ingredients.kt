package com.example.my_shoplist_application.domain.models

data class Ingredients(
    val id: Int,
    val ingredientName: String,
    val ingredientQuantity: Int,
    val ingredientUnit: String,
    var isBought: Boolean
)

