package com.example.my_shoplist_application.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_shoplist_application.BuildConfig
import com.example.my_shoplist_application.domain.api.MainScreenInteractor
import com.example.my_shoplist_application.domain.api.ShoplistScreenInteractor
import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.MeasurementUnit
import com.example.my_shoplist_application.domain.models.Shoplist
import com.example.my_shoplist_application.presentation.model.IngredientListState
import com.example.my_shoplist_application.presentation.model.ShoplistScreenEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class ShoplistScreenViewModel(
    private val shoplistScreenInteractor: ShoplistScreenInteractor,
    private val mainScreenInteractor: MainScreenInteractor
) :
    ViewModel() {
    private val _state = MutableStateFlow<IngredientListState>(IngredientListState())
    val stateIngredient: StateFlow<IngredientListState> get() = _state

    private val _shoplist = MutableStateFlow<Shoplist?>(null)
    val shoplist: StateFlow<Shoplist?> = _shoplist

    private val _isDialogVisible = MutableStateFlow(false) // временно
    val isDialogVisible = _isDialogVisible.asStateFlow()

    private val _isDialogDeleteVisible = MutableStateFlow(false) // временно
    val isDialogDeleteVisible = _isDialogDeleteVisible.asStateFlow()

    fun showDialog() {
        _isDialogVisible.value = true
    }

    fun getShoppingListById(id: Int) {
        viewModelScope.launch {
            mainScreenInteractor.getShoplistById(id)
                .collect { list ->
                    _shoplist.value = list
                }
        }
    }

    fun obtainEvent(event: ShoplistScreenEvent) {
        when (event) {
            is ShoplistScreenEvent.Default -> default(event)

            is ShoplistScreenEvent.OnBackBtnClick -> TODO() //кнопка назад вверху экрана

            is ShoplistScreenEvent.OnAddingIngredientBtnClick -> onAddingIngredientsBtnClick(event)

            is ShoplistScreenEvent.OnContextMenuIconClick -> {// кнопка вызова контекстного меню три точки вверху экрана
                _state.update {
                    it.copy(showContextMenu = true, contextMenuPosition = event.position)
                }
            }

            is ShoplistScreenEvent.OnClearBtnInContextMenuClick -> TODO() // кнопка очистки списка в контекстном меню (нет в фигме, есть в ТЗ)
            is ShoplistScreenEvent.OnDeleteBtnInContextMenuClick -> TODO() // кнопка удаления списка в контекстном меню (нет в фигме, есть в ТЗ)
            is ShoplistScreenEvent.OnRenameBtnInContextMenuClick -> TODO() // кнопка переименования списка в контекстном меню (нет в фигме, есть в ТЗ)
            is ShoplistScreenEvent.OnSortBtnInContextMenuClick -> TODO() // кнопка сортировки списка в алфавитном порядке в контекстном меню (нет в фигме, есть в ТЗ)

            is ShoplistScreenEvent.OnDeleteIngredientSwipeClick -> { // кнопка удаления ингредиента в свайп-меню (нет в фигме, есть в ТЗ)
                viewModelScope.launch {
                    shoplistScreenInteractor.deleteIngredient(ingredient = event.ingredient)
                }
            }

            is ShoplistScreenEvent.OnEditIngredientSwipeClick -> TODO() // кнопка редактирования ингредиента в свайп-меню (нет в фигме, есть в ТЗ)
            is ShoplistScreenEvent.OnIngredientUnitClick -> TODO() // кнопка на панели выбора единицы измерения в статусе экрана "добавление ингредиента" (после нажатия на кнопку добавить продукт)
            is ShoplistScreenEvent.OnMinusIngredientQuantityClick -> TODO() //там же, кнопка "минус количества"
            is ShoplistScreenEvent.OnPlusIngredientQuantityClick -> TODO() //там же, кнопка "плюс количества"

            is ShoplistScreenEvent.OnReadyIngredientBtnClick -> onReadyIngredientsBtnClick(event)

            is ShoplistScreenEvent.OnIsBoughtIngredientClick -> { // флажок товар "куплен" слева от ингредиента
                viewModelScope.launch {
                    runCatching {
                        shoplistScreenInteractor.updateItem(event.ingredient.copy(isBought = !event.ingredient.isBought))
                    }
                }
            }

            is ShoplistScreenEvent.OnSaveShoplistBtnClick -> onSaveShoplistBtnClick(event)

            is ShoplistScreenEvent.UpdateItemName -> updateItemName(event)
        }
    }

    private fun default(event: ShoplistScreenEvent.Default) {
        viewModelScope.launch(Dispatchers.IO) {

            shoplistScreenInteractor.getIngredients(event.listId).collect { items ->
                _state.update { it.copy(ingredients = items) }
            }

            shoplistScreenInteractor.getSuggestions().collect { suggestions ->
                _state.update { it.copy(suggestions = suggestions) }
            }
        }
    }


    private fun onAddingIngredientsBtnClick(event: ShoplistScreenEvent.OnAddingIngredientBtnClick) {
        viewModelScope.launch(Dispatchers.IO) {
            event.listId?.let {
                shoplistScreenInteractor.saveIngredientToDB(
                    Ingredients(
                        ingredientName = event.name,
                        ingredientQuantity = event.quantity,
                        ingredientUnit = event.unit,
                        shopListId = it
                    )
                )
            }
            // Сохраняем имя товара для автоподсказок
            shoplistScreenInteractor.saveSuggestion(event.name)
            _state.update {
                it.copy(
                    newItemName = "",
                    newItemQuantity = "",
                    newItemUnit = MeasurementUnit.PCS
                )
            }
        }
    }

    private fun onReadyIngredientsBtnClick(event: ShoplistScreenEvent.OnReadyIngredientBtnClick) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                shoplistScreenInteractor.saveIngredientToDB(event.ingredient)
            }.onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "saving ingredient to DB: $error")
                } else {
                    // Отправка логов об исключении на сервер
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                shoplistScreenInteractor.saveIngredientToShoplist(
                    event.ingredient,
                    event.shoplist
                )
            }.onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "saving ingredient to shoplist: $error")
                } else {
                    // Отправка логов об исключении на сервер
                }
            }
        }
    }

    private fun onSaveShoplistBtnClick(event: ShoplistScreenEvent.OnSaveShoplistBtnClick) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                shoplistScreenInteractor.createShoplist(
                    event.shoplist
                )
            }.onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "creating shoplist error: $error")
                } else {
                    // Отправка логов об исключении на сервер
                }
            }
        }
    }

    private fun updateItemName(event: ShoplistScreenEvent.UpdateItemName) {
        _state.update { it.copy(newItemName = event.text) }
        if (event.text.isNotEmpty()) {
            viewModelScope.launch {
                val suggestions =
                    shoplistScreenInteractor.getSuggestionsByPrefix(event.text)
                _state.update { it.copy(suggestions = suggestions) }
            }
        }
    }

    fun hideDialog() {
        _isDialogVisible.value = false
        _isDialogDeleteVisible.value = false
    }

    private companion object {
        const val TAG = "ShoplistScreenViewModel"
    }
}
