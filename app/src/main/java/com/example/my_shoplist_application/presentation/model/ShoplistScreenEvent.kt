package com.example.my_shoplist_application.presentation.model

import com.example.my_shoplist_application.domain.models.Ingridients
import com.example.my_shoplist_application.domain.models.Shoplist

sealed interface ShoplistScreenEvent {
    data object Default : ShoplistScreenEvent
    data object OnBackBtnClick : ShoplistScreenEvent
    data object OnContextMenuIconClick : ShoplistScreenEvent
    data object OnSortBtnInContextMenuClick : ShoplistScreenEvent
    data object OnRenameBtnInContextMenuClick : ShoplistScreenEvent
    data object OnDeleteBtnInContextMenuClick : ShoplistScreenEvent
    data object OnClearBtnInContextMenuClick : ShoplistScreenEvent
    class OnEditIngridientSwipeClick(val ingridientId: Int) : ShoplistScreenEvent
    class OnDeleteIngridientSwipeClick(val ingridientId: Int) : ShoplistScreenEvent
    class OnIsBoughtIngridientClick(val ingridientId: Int) : ShoplistScreenEvent
    data object OnAddingIngridientBtnClick : ShoplistScreenEvent
    class OnIngridientUnitClick(val ingridientUnit: String) : ShoplistScreenEvent
    data object OnPlusIngridientQuantityClick : ShoplistScreenEvent
    data object OnMinusIngridientQuantityClick : ShoplistScreenEvent
    class OnReadyIngridientBtnClick(val ingridient: Ingridients) : ShoplistScreenEvent
    class OnSaveShoplistBtnClick(val shoplist: Shoplist) : ShoplistScreenEvent
}