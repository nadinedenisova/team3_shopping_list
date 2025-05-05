package com.example.my_shoplist_application.domain.models

data class Ingredients(
    val id: Int = 0,
    val ingredientName: String,
    val ingredientQuantity: Float,
    val ingredientUnit: MeasurementUnit,
    val shopListId: Int = 0,
    var isBought: Boolean = false
)

