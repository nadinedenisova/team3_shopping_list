package com.example.my_shoplist_application.presentation.model

import androidx.compose.ui.geometry.Offset
import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.MeasurementUnit

sealed interface ShoplistScreenEvent {
    data class Default(val listId: Int) : ShoplistScreenEvent
    data class ShowContextMenu(val position: Offset) : ShoplistScreenEvent
    object HideContextMenu : ShoplistScreenEvent
    data object OnSortBtnInContextMenuClick : ShoplistScreenEvent
    data object OnDeleteBtnInContextMenuClick : ShoplistScreenEvent
    class OnDeleteIngredientSwipeClick(val ingredient: Ingredients) : ShoplistScreenEvent
    class OnUpdateBoughtIngredientClick(val ingredient: Ingredients) : ShoplistScreenEvent
    class OnUpdateAllBoughtIngredientClick(val isChecked: Boolean) : ShoplistScreenEvent
    class OnUpdateIngredientClick(val ingredient: Ingredients) : ShoplistScreenEvent
    object OnAddingIngredientBtnClick : ShoplistScreenEvent  // Добавить продукт
    data class UpdateItemName(val text: String) : ShoplistScreenEvent
    object ShowAddPanel: ShoplistScreenEvent
    object HideAddPanel: ShoplistScreenEvent
    data class ChangeAddUnit(val unit: MeasurementUnit) : ShoplistScreenEvent
    data class ChangeAddQuantity(val quantity: Int) : ShoplistScreenEvent
}