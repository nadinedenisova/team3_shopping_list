package com.example.my_shoplist_application.presentation

import androidx.lifecycle.ViewModel
import com.example.my_shoplist_application.domain.api.ShoplistScreenInteractor
import com.example.my_shoplist_application.presentation.model.ShoplistScreenEvent
import com.example.my_shoplist_application.presentation.model.ShoplistScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ShoplistScreenViewModel(private val shoplistScreenInteractor: ShoplistScreenInteractor) :
    ViewModel() {
    private val _state = MutableStateFlow<ShoplistScreenState>(ShoplistScreenState.NewList)
    val state: StateFlow<ShoplistScreenState> get() = _state

    fun obtainEvent(event: ShoplistScreenEvent) {
        when (event) {
            is ShoplistScreenEvent.Default -> TODO()
            is ShoplistScreenEvent.OnBackBtnClick -> TODO()
            is ShoplistScreenEvent.OnAddingIngridientBtnClick -> TODO()
            is ShoplistScreenEvent.OnClearBtnInContextMenuClick -> TODO()
            is ShoplistScreenEvent.OnContextMenuIconClick -> TODO()
            is ShoplistScreenEvent.OnDeleteBtnInContextMenuClick -> TODO()
            is ShoplistScreenEvent.OnDeleteIngridientSwipeClick -> TODO()
            is ShoplistScreenEvent.OnEditIngridientSwipeClick -> TODO()
            is ShoplistScreenEvent.OnIngridientUnitClick -> TODO()
            is ShoplistScreenEvent.OnIsBoughtIngridientClick -> TODO()
            is ShoplistScreenEvent.OnMinusIngridientQuantityClick -> TODO()
            is ShoplistScreenEvent.OnPlusIngridientQuantityClick -> TODO()
            is ShoplistScreenEvent.OnReadyIngridientBtnClick -> TODO()
            is ShoplistScreenEvent.OnRenameBtnInContextMenuClick -> TODO()
            is ShoplistScreenEvent.OnSaveShoplistBtnClick -> TODO()
            is ShoplistScreenEvent.OnSortBtnInContextMenuClick -> TODO()
        }
    }
}