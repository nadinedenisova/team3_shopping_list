package com.example.my_shoplist_application.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_shoplist_application.domain.api.ShoplistScreenInteractor
import com.example.my_shoplist_application.presentation.model.ShoplistScreenEvent
import com.example.my_shoplist_application.presentation.model.ShoplistScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShoplistScreenViewModel(private val shoplistScreenInteractor: ShoplistScreenInteractor) :
    ViewModel() {
    private val _state =
        MutableStateFlow<ShoplistScreenState.CurrentState>(ShoplistScreenState.CurrentState.EDIT_LIST_STATE)
    val state: StateFlow<ShoplistScreenState.CurrentState> get() = _state

    fun obtainEvent(event: ShoplistScreenEvent) {
        when (event) {
            is ShoplistScreenEvent.Default -> TODO()
            is ShoplistScreenEvent.OnBackBtnClick -> TODO() //кнопка назад вверху экрана
            is ShoplistScreenEvent.OnAddingIngredientBtnClick -> TODO() // кнопка добавления нового продукта внизу экрана
            is ShoplistScreenEvent.OnContextMenuIconClick -> TODO() // кнопка вызова контекстного меню три точки вверху экрана
            is ShoplistScreenEvent.OnClearBtnInContextMenuClick -> TODO() // кнопка очистки списка в контекстном меню (нет в фигме, есть в ТЗ)
            is ShoplistScreenEvent.OnDeleteBtnInContextMenuClick -> TODO() // кнопка удаления списка в контекстном меню (нет в фигме, есть в ТЗ)
            is ShoplistScreenEvent.OnRenameBtnInContextMenuClick -> TODO() // кнопка переименования списка в контекстном меню (нет в фигме, есть в ТЗ)
            is ShoplistScreenEvent.OnSortBtnInContextMenuClick -> TODO() // кнопка сортировки списка в алфавитном порядке в контекстном меню (нет в фигме, есть в ТЗ)
            is ShoplistScreenEvent.OnDeleteIngredientSwipeClick -> TODO() // кнопка удаления ингредиента в свайп-меню (нет в фигме, есть в ТЗ)
            is ShoplistScreenEvent.OnEditIngredientSwipeClick -> TODO() // кнопка редактирования ингредиента в свайп-меню (нет в фигме, есть в ТЗ)
            is ShoplistScreenEvent.OnIngredientUnitClick -> TODO() // кнопка на панели выбора единицы измерения в статусе экрана "добавление ингредиента" (после нажатия на кнопку добавить продукт)
            is ShoplistScreenEvent.OnMinusIngredientQuantityClick -> TODO() //там же кнопка "минус количества"
            is ShoplistScreenEvent.OnPlusIngredientQuantityClick -> TODO() //там же кнопка "плюс количества"
            is ShoplistScreenEvent.OnReadyIngredientBtnClick -> TODO() // там же кнопка "Готово"
            is ShoplistScreenEvent.OnIsBoughtIngredientClick -> TODO() // флажок товар "куплен" слева от ингредиента


            is ShoplistScreenEvent.OnSaveShoplistBtnClick -> { // кнопка сохранить список внизу экрана
                viewModelScope.launch(Dispatchers.IO) {
                    shoplistScreenInteractor.createShoplist(
                        event.shoplist
                    )
                }
            }


        }
    }
}