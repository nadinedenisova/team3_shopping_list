package com.example.my_shoplist_application.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_shoplist_application.domain.api.MainScreenInteractor
import com.example.my_shoplist_application.presentation.model.MainScreenAction
import com.example.my_shoplist_application.presentation.model.MainScreenEvent
import com.example.my_shoplist_application.presentation.model.MainScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel(private val mainScreenInteractor: MainScreenInteractor) : ViewModel() {
    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Default)
    val state: StateFlow<MainScreenState> get() = _state
    private val _action = MutableStateFlow<MainScreenAction?>(null)
    val action: StateFlow<MainScreenAction?> get() = _action

    fun obtainEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.Default -> {
                viewModelScope.launch(Dispatchers.IO) { mainScreenInteractor.getShoplists() }
            }

            is MainScreenEvent.OnBtnNewShopListClick -> {
                // навигация на экран создания списка покупок
            }

            is MainScreenEvent.OnDeleteShopListClick -> {
                viewModelScope.launch {
                    _action.update {
                        MainScreenAction.ShowDeletingShoplistConfirmation
                    }
                }
                // viewModelScope.launch(Dispatchers.IO) { mainScreenInteractor.deleteShoplist(event.shoplistId) }

            }

            is MainScreenEvent.OnRenameShopListClick -> {
                val newShoplistName: String = "" // реализовать получение нового названия списка
                viewModelScope.launch(Dispatchers.IO) {
                    mainScreenInteractor.renameShoplist(
                        event.shoplistId,
                        newShoplistName
                    )
                }
            }

            is MainScreenEvent.OnDoubleShopListClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    mainScreenInteractor.doubleShoplist(
                        event.shoplistId
                    )
                }
            }

            is MainScreenEvent.OnShopListClick -> {
                // навигация на экран редактирования списка покупок
            }

        }
    }
}