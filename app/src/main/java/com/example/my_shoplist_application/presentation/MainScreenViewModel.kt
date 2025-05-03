package com.example.my_shoplist_application.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_shoplist_application.BuildConfig
import com.example.my_shoplist_application.domain.api.MainScreenInteractor
import com.example.my_shoplist_application.domain.db.Result
import com.example.my_shoplist_application.presentation.model.MainScreenEvent
import com.example.my_shoplist_application.presentation.model.MainScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class MainScreenViewModel(private val mainScreenInteractor: MainScreenInteractor) : ViewModel() {
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> get() = _state

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
                // навигация на экран создания списка покупок
            }

            is MainScreenEvent.OnDeleteShopListConfirmClick -> {
                viewModelScope.launch {
                    _state.update { it.copy(isDialogVisible = false) }
                }
                //viewModelScope.launch(Dispatchers.IO) { mainScreenInteractor.deleteShoplist(event.shoplistId) }
            }

            is MainScreenEvent.OnRenameShopListClick -> {

                viewModelScope.launch(Dispatchers.IO) {
                    runCatching {
                        mainScreenInteractor.renameShoplist(
                            event.shoplistId,
                            event.shoplistName
                        )
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
        }
    }

    private companion object {
        const val TAG = "MainScreenViewModel"
    }
}