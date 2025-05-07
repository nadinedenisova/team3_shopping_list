package com.example.my_shoplist_application.data.entity

import android.database.sqlite.SQLiteException
import com.example.my_shoplist_application.BuildConfig
import com.example.my_shoplist_application.common.InvalidDatabaseStateException
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

        return if (retryNumber != 3) {
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
                1 -> appDataBase.shoplistDao().deleteShoplist(shoplistId)
                2 -> appDataBase.shoplistDao().renameShoplist(shoplistId, shoplistName)
                3 -> appDataBase.shoplistDao()
                    .insertShoplist(appDataBase.shoplistDao().getShoplistById(shoplistId))

                4 -> appDataBase.shoplistDao().onTogglePinShoplist(
                    shoplistId,
                    !appDataBase.shoplistDao().getShoplistById(shoplistId).isPinned
                )

                else -> {}
            }
            Unit
        }

        if (result.isSuccess) return result

        return if (retryNumber != 3) {
            interactWithDb(shoplistId, choice, shoplistName, retryNumber + 1)
        } else {
            result
        }
    }

    override suspend fun deleteShoplist(shoplistId: Int, retryNumber: Int): kotlin.Result<Unit> {
        return interactWithDb(shoplistId, 1)
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

        return interactWithDb(shoplistId, 2, shoplistName)
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

        return interactWithDb(shoplistId, 3)
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
        return interactWithDb(shoplistId, 4)
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

    override suspend fun getShoplist(retryNumber: Int): Flow<List<Shoplist>> = flow {
        val result = runCatching {
            val shoplists = appDataBase.shoplistDao().getShoplists()
            if (shoplists.isEmpty()) {
                throw InvalidDatabaseStateException(message = "no shoplists yet")
            }
        }
    }

    override suspend fun saveShopList(list: Shoplist): Long {
        return appDataBase.shoplistDao().insertList(shoplistDbConvertor.map(list))
    }

    override suspend fun getShoplistById(id: Int): Flow<Shoplist> = flow {
        val shoplistEntity = appDataBase.shoplistDao().getShoplistById(id)
        emit(shoplistEntity.let { shoplistDbConvertor.map(it) })
    }
}