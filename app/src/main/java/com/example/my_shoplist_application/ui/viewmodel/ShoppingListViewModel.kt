package com.example.my_shoplist_application.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ShoppingList(
    val id: String,
    val name: String,
    val isPinned: Boolean = false
)

class ShoppingListViewModel: ViewModel() {
    // Состояние списка покупок
    private val _shoppingLists = MutableStateFlow<List<ShoppingList>>(
        listOf(
            ShoppingList("1", "Ингредиенты для торта", true),
            ShoppingList("2", "Украшения на Новый год🎄"),
            ShoppingList("3", "Канцелярия детям")
        )
    )
    val shoppingLists = _shoppingLists.asStateFlow()

    // Состояние диалога
    private val _isDialogVisible = MutableStateFlow(false)
    val isDialogVisible = _isDialogVisible.asStateFlow()

    // Закрепление элемента
    fun togglePin(id: String) {
        _shoppingLists.update { lists ->
            lists.map { list ->
                if (list.id == id) {
                    list.copy(isPinned = !list.isPinned)
                } else {
                    list
                }
            }.sortedByDescending { it.isPinned }
        }
    }

    // Удаление элемента
    fun deleteList(id: String) {
        _shoppingLists.update { lists ->
            lists.filter { it.id != id }
        }
    }

    // Добавление нового элемента
    fun addNewList(name: String) {
        if (name.isNotBlank()) {
            val newId = (_shoppingLists.value.size + 1).toString()
            val newList = ShoppingList(newId, name)

            _shoppingLists.update { currentLists ->
                (currentLists + newList).sortedByDescending { it.isPinned }
            }
        }
    }

    // Управление диалогом
    fun showDialog() {
        _isDialogVisible.value = true
    }

    fun hideDialog() {
        _isDialogVisible.value = false
    }
}