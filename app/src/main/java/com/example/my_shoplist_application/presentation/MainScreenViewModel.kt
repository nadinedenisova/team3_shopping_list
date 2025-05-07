package com.example.my_shoplist_application.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_shoplist_application.BuildConfig
import com.example.my_shoplist_application.domain.api.MainScreenInteractor
import com.example.my_shoplist_application.domain.db.Result
import com.example.my_shoplist_application.domain.models.Shoplist
import com.example.my_shoplist_application.presentation.model.MainScreenEvent
import com.example.my_shoplist_application.presentation.model.MainScreenState
import com.example.my_shoplist_application.presentation.model.ShoppingListEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class MainScreenViewModel(private val mainScreenInteractor: MainScreenInteractor) : ViewModel() {
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<ShoppingListEvent>()
    val events = _events.asSharedFlow()

    init {
        obtainEvent(MainScreenEvent.Default)
    }

    fun obtainEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.Default -> {
                viewModelScope.launch(Dispatchers.IO) {
                    runCatching {
                        mainScreenInteractor.getShoplists().collect { result ->
                            when (result) {
                                is Result.Success -> _state.update { it.copy(shoplists = result.data) }
                                is Result.Error<*, *> -> TODO()
                            }

                        }

                    }.onFailure { error ->
                        if (error is CancellationException) {
                            throw CancellationException()
                        }
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "getting shoplist error: $error")
                        } else {
                            // Отправка логов об исключении на сервер
                        }
                    }
                }
            }

            is MainScreenEvent.OnBtnNewShopListClick -> {
                _state.update { it.copy(isDialogAddingItemVisible = true) }
            }

            is MainScreenEvent.OnDeleteShopListConfirmClick -> {
                viewModelScope.launch {
                    _state.update { it.copy(isDialogVisible = false) }
                }
                viewModelScope.launch(Dispatchers.IO) { mainScreenInteractor.deleteShoplist(event.shoplistId) }
            }

            is MainScreenEvent.OnRenameShopListClick -> {

                viewModelScope.launch(Dispatchers.IO) {
                    runCatching {
                        mainScreenInteractor.renameShoplist(
                            event.shoplistId,
                            event.shoplistName
                        )
                        obtainEvent(MainScreenEvent.Default)

                    }.onFailure { error ->
                        if (error is CancellationException) {
                            throw CancellationException()
                        }
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "renaming shoplist error: $error")
                        } else {
                            // Отправка логов об исключении на сервер
                        }
                    }
                }
            }


            is MainScreenEvent.OnDoubleShopListClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    runCatching {
                        mainScreenInteractor.doubleShoplist(
                            event.shoplistId
                        )
                        obtainEvent(MainScreenEvent.Default)
                    }.onFailure { error ->
                        if (error is CancellationException) {
                            throw CancellationException()
                        }
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "doubling shoplist error: $error")
                        } else {
                            // Отправка логов об исключении на сервер
                        }
                    }
                }

            }

            is MainScreenEvent.OnShopListClick -> {
                // навигация на экран редактирования списка покупок
            }

            is MainScreenEvent.OnTogglePinListClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    runCatching {
                        mainScreenInteractor.onTogglePinList(
                            event.shoplistId
                        )
                        obtainEvent(MainScreenEvent.Default)
                    }.onFailure { error ->
                        if (error is CancellationException) {
                            throw CancellationException()
                        }
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "pinning/unpinning shoplist error: $error")
                        } else {
                            // Отправка логов об исключении на сервер
                        }
                    }
                }
            }

            MainScreenEvent.OnDeleteShopListClick -> {
                _state.update { it.copy(isDialogVisible = true) }
            }

            MainScreenEvent.OnDismissDeleteShopListClick -> {
                _state.update { it.copy(isDialogVisible = false) }
            }

            MainScreenEvent.OnCloseAddingWindow -> {
                _state.update { it.copy(isDialogAddingItemVisible = false) }
            }

            is MainScreenEvent.Add -> {
                viewModelScope.launch {
                    val newId = mainScreenInteractor.saveShopList(Shoplist(shoplistName = event.name))
                    _events.emit(
                        ShoppingListEvent.NavigateToIngredients(
                            newId.toInt(),
                        )
                    )
                }
            }
        }
    }

    private companion object {
        const val TAG = "MainScreenViewModel"
    }
}