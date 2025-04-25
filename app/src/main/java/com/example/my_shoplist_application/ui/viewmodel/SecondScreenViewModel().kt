package com.example.my_shoplist_application.ui.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.plus
import kotlin.collections.sortedByDescending


data class ShoppingListItem(
    val id: String,
    val name: String,
    val quantity: Int = 1,
    val unit: MeasurementUnit,
    val isPurchased: Boolean = false
)


data class ShoppingItem(
    val id: Int = 0,
    val name: String,
    val quantity: Float,
    val unit: MeasurementUnit = MeasurementUnit.PCS,
    val isPurchased: Boolean = false
)

enum class MeasurementUnit(val label: String) {
    PCS("шт"),
    KG("кг"),
    L("л"),
    ML("мл"),
    G("г")
}

class SecondScreenViewModel(): ViewModel() {

    private val _shoppingLists = MutableStateFlow<List<ShoppingListItem>>(
        listOf(
            ShoppingListItem("1", "Мука", 1, unit = MeasurementUnit.PCS, false),
            ShoppingListItem("2", "Сахар",1, unit = MeasurementUnit.KG, false),
            ShoppingListItem("3", "Молоко",1, unit = MeasurementUnit.L, false))
        )

    val shoppingLists = _shoppingLists.asStateFlow()

    private val _items = MutableStateFlow(listOf<ShoppingListItem>())
    val items: StateFlow<List<ShoppingListItem>> = _items

    // Для хранения предлагаемых имен продуктов
    private val _suggestedNames = MutableStateFlow(listOf<String>())
    val suggestedNames: StateFlow<List<String>> = _suggestedNames

    private val _switchStatus = MutableStateFlow<Boolean>(false)
    val switchStatus = _switchStatus.asStateFlow()

    private fun getSwitchStatus() {
        _switchStatus.value = false
    }

    fun switch(isChecked: Boolean) {
       // providerSwitchStatus.switchIsChecked(isChecked)
        _switchStatus.value = isChecked
    }

    // Добавление элемента в список
    fun addItem(name: String, quantity: Int, unit: MeasurementUnit) {
        if (name.isNotBlank()) {
            val newId = (_shoppingLists.value.size + 1).toString()
            val newList = ShoppingListItem(
                id =  newId,
               name= name,
               quantity = quantity,
               unit = unit )

            _shoppingLists.update { currentLists ->
                (currentLists + newList).sortedByDescending { it.isPurchased }
            }
        }

      //  _items.value = _items.value + newItem
        saveItemName(name) // Сохраняем имя для будущих подсказок
    }

    // Обновление элемента
    fun updateItem(item: ShoppingListItem) {
        _items.value = _items.value.map {
            if (it.id == item.id) item else it
        }
    }

    // Удаление элемента
    fun removeItem(id: String) {
        _items.value = _items.value.filter { it.id contentEquals id }
    }

    // Переключение статуса "куплено"
    fun togglePurchased(id: String) {
        _items.value = _items.value.map {
            if (it.id contentEquals id ) it.copy(isPurchased = !it.isPurchased) else it
        }
    }

    // Очистка списка от купленных товаров
    fun clearPurchasedItems() {
        _items.value = _items.value.filter { !it.isPurchased }
    }

    // Сортировка списка по алфавиту
    fun sortItemsAlphabetically() {
        _items.value = _items.value.sortedBy { it.name }
    }

    // Сохранение имени товара для подсказок
    private fun saveItemName(name: String) {
        if (!suggestedNames.value.contains(name)) {
            _suggestedNames.value = _suggestedNames.value + name
        }
    }

    // Получение подсказок для имени товара
    fun getSuggestions(input: String) {
        viewModelScope.launch {
            _suggestedNames.value = _suggestedNames.value.filter {
                it.contains(input, ignoreCase = true)
            }
        }
    }
}
