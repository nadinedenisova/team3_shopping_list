package com.example.my_shoplist_application.presentation.model

import androidx.compose.ui.geometry.Offset
import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.MeasurementUnit
import com.example.my_shoplist_application.domain.models.Shoplist

sealed interface ShoplistScreenEvent {
    data class Default(val listId: Int) : ShoplistScreenEvent
    data object OnBackBtnClick : ShoplistScreenEvent
    data class OnContextMenuIconClick(val position: Offset) : ShoplistScreenEvent
    data object OnSortBtnInContextMenuClick : ShoplistScreenEvent
    data object OnRenameBtnInContextMenuClick : ShoplistScreenEvent
    data object OnDeleteBtnInContextMenuClick : ShoplistScreenEvent
    data object OnClearBtnInContextMenuClick : ShoplistScreenEvent
    class OnEditIngredientSwipeClick(val ingredientId: Int) : ShoplistScreenEvent
    class OnDeleteIngredientSwipeClick(val ingredient: Ingredients) : ShoplistScreenEvent
    class OnIsBoughtIngredientClick(val ingredient: Ingredients, val shopList: Shoplist) : ShoplistScreenEvent
    data class OnAddingIngredientBtnClick(val name: String, val quantity: Float, val unit: MeasurementUnit, val listId: Int?) : ShoplistScreenEvent
    class OnIngredientUnitClick(val ingredientUnit: String) : ShoplistScreenEvent
    data object OnPlusIngredientQuantityClick : ShoplistScreenEvent
    data object OnMinusIngredientQuantityClick : ShoplistScreenEvent
    class OnReadyIngredientBtnClick(val ingredient: Ingredients, val shoplist: Shoplist) : ShoplistScreenEvent
    class OnSaveShoplistBtnClick(val shoplist: Shoplist) : ShoplistScreenEvent
    data class UpdateItemName(val text: String) : ShoplistScreenEvent
}