package com.example.my_shoplist_application.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_shoplist_application.domain.api.MainScreenInteractor
import com.example.my_shoplist_application.domain.api.ShoplistScreenInteractor
import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.MeasurementUnit
import com.example.my_shoplist_application.domain.models.Shoplist
import com.example.my_shoplist_application.presentation.model.IngredientListState
import com.example.my_shoplist_application.presentation.model.ShoplistScreenEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShoplistScreenViewModel(
    private val shoplistScreenInteractor: ShoplistScreenInteractor,
    private val mainScreenInteractor: MainScreenInteractor
) :
    ViewModel() {
    private val _state = MutableStateFlow<IngredientListState>(IngredientListState())
    val stateIngredient = _state.asStateFlow()

    private val _shoplist = MutableStateFlow<Shoplist?>(null)
    val shoplist = _shoplist.asStateFlow()


    fun getShoppingListById(id: Int) {
        viewModelScope.launch {
            mainScreenInteractor.getShoplistById(id)
                .collect { list ->
                    _shoplist.value = list
                    _state.update { it.copy(isSelectProducts = list?.isSelectProducts == true) }
                }
        }
    }

    fun obtainEvent(event: ShoplistScreenEvent) {
        when (event) {
            is ShoplistScreenEvent.Default -> {
                viewModelScope.launch(Dispatchers.IO) {
                    shoplistScreenInteractor.getIngredients(event.listId).collect { items ->
                        _state.update { it.copy(ingredients = items) }
                    }
                    shoplistScreenInteractor.getSuggestions().collect { suggestions ->
                        _state.update { it.copy(suggestions = suggestions) }
                    }
                }
            }

            is ShoplistScreenEvent.OnAddingIngredientBtnClick -> {// кнопка добавления нового продукта внизу экрана
                val listId = shoplist.value?.id ?: 0
                val name = _state.value.newItemName.trim()
                if (name.isNotBlank()) {
                    if (_state.value.isSelectProducts== false) {
                        viewModelScope.launch(Dispatchers.IO) {
                            shoplistScreenInteractor.saveIngredientToDB(
                                Ingredients(
                                    ingredientName = name,
                                    ingredientQuantity = _state.value.newItemQuantity,
                                    ingredientUnit = _state.value.newItemUnit,
                                    shopListId = shoplist.value?.id ?: 0
                                )
                            )
                            shoplistScreenInteractor.saveSuggestion(name)
                            _state.update {
                                it.copy(
                                    showAddPanel = false,
                                    //   showRenamePanel = false,
                                    editingIngredientId = 0,
                                    newItemName = "",
                                    newItemQuantity = 0,
                                    newItemUnit = MeasurementUnit.PCS
                                )
                            }
                        }

                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            shoplistScreenInteractor.updateIsSelectProducts(
                                listid = listId,
                                false
                            )
                            shoplistScreenInteractor.saveIngredientToDB(
                                Ingredients(
                                    ingredientName = name,
                                    ingredientQuantity = _state.value.newItemQuantity,
                                    ingredientUnit = _state.value.newItemUnit,
                                    shopListId = shoplist.value?.id ?: 0
                                )
                            )
                            shoplistScreenInteractor.saveSuggestion(name)
                            _state.update {
                                it.copy(
                                    showAddPanel = false,
                                    //   showRenamePanel = false,
                                    editingIngredientId = 0,
                                    newItemName = "",
                                    newItemQuantity = 0,
                                    newItemUnit = MeasurementUnit.PCS
                                )
                            }
                            _state.update { it.copy(isSelectProducts = false) }
                        }


                    }
                }
            }

            is ShoplistScreenEvent.ShowContextMenu -> {// кнопка вызова контекстного меню три точки вверху экрана
                _state.update {
                    it.copy(showContextMenu = true)
                }
            }

            is ShoplistScreenEvent.HideContextMenu -> {
                _state.update {
                    it.copy(showContextMenu = false)
                }
            }


            is ShoplistScreenEvent.OnDeleteBtnInContextMenuClick -> {// кнопка удаления списка в контекстном меню (нет в фигме, есть в ТЗ)
                val listId = shoplist.value?.id ?: 0
                viewModelScope.launch {
                    shoplistScreenInteractor.deleteBoughtItems()
                    shoplistScreenInteractor.updateAllBoughtStatus(listId, false)
                }
                _state.update { it.copy(isSelectProducts = false) }

            }

            is ShoplistScreenEvent.OnSortBtnInContextMenuClick -> {// кнопка сортировки списка в алфавитном порядке в контекстном меню (нет в фигме, есть в ТЗ)
                _state.update { state ->
                    state.copy(
                        ingredients = state.ingredients.sortedBy { it.ingredientName.lowercase() }
                    )
                }
            }

            is ShoplistScreenEvent.OnDeleteIngredientSwipeClick -> { // кнопка удаления ингредиента в свайп-меню (нет в фигме, есть в ТЗ)
                viewModelScope.launch {
                    shoplistScreenInteractor.deleteIngredient(ingredient = event.ingredient)
                }

            }

            is ShoplistScreenEvent.OnUpdateBoughtIngredientClick -> { // флажок товар "куплен" слева от ингредиента
                viewModelScope.launch {
                    runCatching {
                        shoplistScreenInteractor.updateItem(event.ingredient.copy(isBought = !event.ingredient.isBought))
                    }
                }
            }

            is ShoplistScreenEvent.OnUpdateAllBoughtIngredientClick -> { // все флажки товар "куплен" слева от ингредиента
                val newValue = event.isChecked
                val listId = shoplist.value?.id ?: 0
                _state.update { it.copy(isSelectProducts = newValue) }
                viewModelScope.launch(Dispatchers.IO) {
                    shoplistScreenInteractor.updateAllBoughtStatus(listId, newValue)
                }
            }

            is ShoplistScreenEvent.UpdateItemName -> {// подсказка при вводе +
                _state.update { it.copy(newItemName = event.text) }
                if (event.text.isNotEmpty()) {
                    viewModelScope.launch {
                        val suggestions =
                            shoplistScreenInteractor.getSuggestionsByPrefix(event.text)
                        _state.update { it.copy(suggestions = suggestions) }
                    }
                }
            }

            ShoplistScreenEvent.ShowAddPanel -> {
                _state.update {
                    it.copy(showAddPanel = true)
                }
            }

            ShoplistScreenEvent.HideAddPanel -> {
                _state.update {
                    it.copy(showAddPanel = false)
                }
            }

            is ShoplistScreenEvent.ChangeAddUnit -> {
                _state.value = _state.value.copy(newItemUnit = event.unit)
                Log.i("LigText", "${event.unit}")
            }

            is ShoplistScreenEvent.ChangeAddQuantity -> {
                _state.value = _state.value.copy(newItemQuantity = event.quantity)
                Log.i("LigText", "${event.quantity}")
            }

            is ShoplistScreenEvent.OnUpdateIngredientClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    shoplistScreenInteractor.updateItem(event.ingredient)
                    // Обновите состояние списка
                    _state.update { state ->
                        state.copy(
                            ingredients = state.ingredients.map {
                                if (it.id == event.ingredient.id) event.ingredient else it
                            }
                        )
                    }
                }
            }
        }
    }

}
