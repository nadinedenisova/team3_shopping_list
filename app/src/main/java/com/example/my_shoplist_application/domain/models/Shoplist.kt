package com.example.my_shoplist_application.domain.models

data class Shoplist(
    val id: Int = 0,
    val shoplistName: String,
    val ingredientsIdList: List<Int> = emptyList<Int>(),
    var isPinned: Boolean = false
)
