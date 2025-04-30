package com.example.my_shoplist_application.data.entity

import android.database.sqlite.SQLiteException
import com.example.my_shoplist_application.BuildConfig
import com.example.my_shoplist_application.data.convertors.ShoplistDbConvertor
import com.example.my_shoplist_application.data.dao.ShoplistDao
import com.example.my_shoplist_application.domain.db.MainScreenListError
import com.example.my_shoplist_application.domain.db.MainScreenRepository
import com.example.my_shoplist_application.domain.db.Result
import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainScreenRepositoryImpl(
    private val shoplistDao: ShoplistDao,
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
            shoplistDao.getShoplists()
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
                1 -> shoplistDao.deleteShoplist(shoplistId)
                2 -> shoplistDao.renameShoplist(shoplistId, shoplistName)
                3 -> shoplistDao.insertShoplist(shoplistDao.getShoplistById(shoplistId))
            }
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

    private fun convertFromShoplistEntity(shoplists: List<ShoplistEntity>): List<Shoplist> {
        return shoplists.map { shoplist -> shoplistDbConvertor.map(shoplist) }
    }
}