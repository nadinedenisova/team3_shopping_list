package com.example.my_shoplist_application.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_shoplist_application.BuildConfig
import com.example.my_shoplist_application.domain.api.MainScreenInteractor
import com.example.my_shoplist_application.presentation.model.MainScreenAction
import com.example.my_shoplist_application.presentation.model.MainScreenEvent
import com.example.my_shoplist_application.presentation.model.MainScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class MainScreenViewModel(private val mainScreenInteractor: MainScreenInteractor) : ViewModel() {
    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Default)
    val state: StateFlow<MainScreenState> get() = _state
    private val _action = MutableStateFlow<MainScreenAction?>(null)
    val action: StateFlow<MainScreenAction?> get() = _action

    fun obtainEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.Default -> {
                viewModelScope.launch(Dispatchers.IO) {
                    runCatching {
                        mainScreenInteractor.getShoplists()

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
                    runCatching {
                        mainScreenInteractor.renameShoplist(
                            event.shoplistId,
                            newShoplistName
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

        }
    }

    private companion object {
        const val TAG = "MainScreenViewModel"
    }
}