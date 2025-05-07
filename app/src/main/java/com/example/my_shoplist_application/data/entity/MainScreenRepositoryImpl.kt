package com.example.my_shoplist_application.data.entity

import android.database.sqlite.SQLiteException
import com.example.my_shoplist_application.BuildConfig
import com.example.my_shoplist_application.data.convertors.ShoplistDbConvertor
import com.example.my_shoplist_application.db.AppDataBase
import com.example.my_shoplist_application.domain.db.MainScreenListError
import com.example.my_shoplist_application.domain.db.MainScreenRepository
import com.example.my_shoplist_application.domain.db.Result
import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainScreenRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val shoplistDbConvertor: ShoplistDbConvertor
) : MainScreenRepository {


    override suspend fun getShoplists(retryNumber: Int): Flow<Result<List<Shoplist>, MainScreenListError>> =
        flow {
            getShoplistsFromDb().onSuccess { data ->
                if (data.isEmpty()) {
                    emit(
                        Result.Error(
                            MainScreenListError.NotFoundError
                        )
                    )
                } else {
                    emit(Result.Success(convertFromShoplistEntity(data)))
                }
            }.onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (error is SQLiteException) {
                    emit(
                        Result.Error(
                            MainScreenListError.DataBaseError
                        )
                    )
                } else {
                    emit(
                        Result.Error(
                            MainScreenListError.UnknownError
                        )
                    )
                }
            }
        }


    private suspend fun getShoplistsFromDb(retryNumber: Int = 0): kotlin.Result<List<ShoplistEntity>> {
        val result = runCatching {
            appDataBase.shoplistDao().getShoplists()
        }
        if (result.isSuccess) return result

        return if (retryNumber != MAX_RETRY_NUMBER) {
            getShoplistsFromDb(retryNumber + 1)
        } else {
            result
        }
    }

    private suspend fun interactWithDb(
        shoplistId: Int,
        choice: Int,
        shoplistName: String = "",
        retryNumber: Int = 0,

        ): kotlin.Result<Unit> {
        val result = runCatching {
            when (choice) {
                DELETE_CHOICE -> appDataBase.shoplistDao().deleteShoplist(shoplistId)
                RENAME_CHOICE -> appDataBase.shoplistDao().renameShoplist(shoplistId, shoplistName)
                DOUBLE_CHOICE -> {
                    val oldShoplistEntity = appDataBase.shoplistDao().getShoplistById(shoplistId)
                    val newShopListEntity = ShoplistEntity(
                        shoplistId = oldShoplistEntity.shoplistId + 1,
                        shoplistName = oldShoplistEntity.shoplistName,
                        ingredientIdsList = oldShoplistEntity.ingredientIdsList,
                        addedAt = oldShoplistEntity.addedAt,
                        isPinned = oldShoplistEntity.isPinned
                    )
                    appDataBase.shoplistDao()
                        .insertShoplist(newShopListEntity)
                }

                TOGGLE_PIN_CHOICE -> appDataBase.shoplistDao().onTogglePinShoplist(
                    shoplistId,
                    !appDataBase.shoplistDao().getShoplistById(shoplistId).isPinned
                )

                else -> {}
            }
            Unit
        }

        if (result.isSuccess) return result

        return if (retryNumber != MAX_RETRY_NUMBER) {
            interactWithDb(shoplistId, choice, shoplistName, retryNumber + 1)
        } else {
            result
        }
    }

    override suspend fun deleteShoplist(shoplistId: Int, retryNumber: Int): kotlin.Result<Unit> {
        return interactWithDb(shoplistId, DELETE_CHOICE)
            .onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    error.printStackTrace()
                }
            }
    }

    override suspend fun renameShoplist(
        shoplistId: Int,
        shoplistName: String,
        retryNumber: Int
    ): kotlin.Result<Unit> {

        return interactWithDb(shoplistId, RENAME_CHOICE, shoplistName)
            .onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    error.printStackTrace()
                }
            }
    }

    override suspend fun doubleShoplist(shoplistId: Int, retryNumber: Int): kotlin.Result<Unit> {

        return interactWithDb(shoplistId, DOUBLE_CHOICE)
            .onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    error.printStackTrace()
                }
            }
    }

    override suspend fun onToggleShoplist(shoplistId: Int, retryNumber: Int): kotlin.Result<Unit> {
        return interactWithDb(shoplistId, TOGGLE_PIN_CHOICE)
            .onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    error.printStackTrace()
                }
            }
    }

    private fun convertFromShoplistEntity(shoplists: List<ShoplistEntity>): List<Shoplist> {
        return shoplists.map { shoplist -> shoplistDbConvertor.map(shoplist) }
    }


    override suspend fun saveShopList(list: Shoplist): Long {
        return appDataBase.shoplistDao().insertList(shoplistDbConvertor.map(list))
    }

    override suspend fun getShoplistById(id: Int): Flow<Shoplist> = flow {
        val shoplistEntity = appDataBase.shoplistDao().getShoplistById(id)
        emit(shoplistEntity.let { shoplistDbConvertor.map(it) })
    }

    companion object {
        private const val DELETE_CHOICE = 1
        private const val RENAME_CHOICE = 2
        private const val DOUBLE_CHOICE = 3
        private const val TOGGLE_PIN_CHOICE = 4
        private const val MAX_RETRY_NUMBER = 3
    }

}
