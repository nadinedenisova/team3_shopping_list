package com.example.my_shoplist_application.presentation.model

import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.Shoplist

sealed interface ShoplistScreenEvent {
    data object Default : ShoplistScreenEvent
    data object OnBackBtnClick : ShoplistScreenEvent
    data object OnContextMenuIconClick : ShoplistScreenEvent
    data object OnSortBtnInContextMenuClick : ShoplistScreenEvent
    data object OnRenameBtnInContextMenuClick : ShoplistScreenEvent
    data object OnDeleteBtnInContextMenuClick : ShoplistScreenEvent
    data object OnClearBtnInContextMenuClick : ShoplistScreenEvent
    class OnEditIngredientSwipeClick(val ingredientId: Int) : ShoplistScreenEvent
    class OnDeleteIngredientSwipeClick(val ingredientId: Int) : ShoplistScreenEvent
    class OnIsBoughtIngredientClick(val ingredient: Ingredients, val shoplist: Shoplist) : ShoplistScreenEvent
    data object OnAddingIngredientBtnClick : ShoplistScreenEvent
    class OnIngredientUnitClick(val ingredientUnit: String) : ShoplistScreenEvent
    data object OnPlusIngredientQuantityClick : ShoplistScreenEvent
    data object OnMinusIngredientQuantityClick : ShoplistScreenEvent
    class OnReadyIngredientBtnClick(val ingredient: Ingredients, val shoplist: Shoplist) : ShoplistScreenEvent
    class OnSaveShoplistBtnClick(val shoplist: Shoplist) : ShoplistScreenEvent
}