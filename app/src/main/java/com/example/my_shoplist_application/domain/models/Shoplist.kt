package com.example.my_shoplist_application.domain.models

data class Shoplist(
    val id: Int,
    val shoplistName: String,
    val ingridientsList: MutableList<Ingridients>,
    var isPinned: Boolean = false
)
