package com.example.my_shoplist_application.presentation.model

import com.example.my_shoplist_application.domain.models.Shoplist

data class MainScreenState(
    val shoplists: List<Shoplist> = emptyList(),
    val isDialogVisible: Boolean = false,
    val isDialogAddingItemVisible: Boolean = false
)

