package com.example.my_shoplist_application.domain.models

data class Ingridients(
    val id: Int,
    val ingridientName: String,
    val ingridientQuantity: Int,
    val ingridientUnit: String,
    var isBought: Boolean
)
