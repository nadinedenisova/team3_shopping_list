package com.example.my_shoplist_application.data.entity

import android.content.Context
import android.widget.Toast
import com.example.my_shoplist_application.BuildConfig
import com.example.my_shoplist_application.common.InvalidDatabaseStateException
import com.example.my_shoplist_application.data.convertors.ShoplistDbConvertor
import com.example.my_shoplist_application.db.AppDataBase
import com.example.my_shoplist_application.domain.db.MainScreenRepository
import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainScreenRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val shoplistDbConvertor: ShoplistDbConvertor,
    private val context: Context
) : MainScreenRepository {

    override suspend fun saveShopList(list: Shoplist): Long {
        return appDataBase.shoplistDao().insertList(shoplistDbConvertor.map(list))
    }

    override suspend fun getShoplistById(id: Int): Flow<Shoplist> = flow {
        val shoplistEntity = appDataBase.shoplistDao().getShoplistById(id)
        emit( shoplistEntity.let { shoplistDbConvertor.map(it) })
    }

    override suspend fun getShoplists(retryNumber: Int): Flow<List<Shoplist>> = flow {
        val result = runCatching {
            val shoplists = appDataBase.shoplistDao().getShoplists()
            if (shoplists.isEmpty()) {
                throw InvalidDatabaseStateException(message = "no shoplists yet")
            }
            emit(convertFromShoplistEntity(shoplists))
        }.onFailure { error ->
            if (BuildConfig.DEBUG) {
                error.printStackTrace()
            }
        }
        if (result.isFailure && retryNumber != 3) {
            getShoplists(retryNumber + 1)
        } else if (result.isFailure) {
            Toast.makeText(
                context,
                "Техническая проблема с базой данных",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override suspend fun deleteShoplist(shoplistId: Int, retryNumber: Int): Result<Unit> {
        var result: Result<Unit> = runCatching {
            appDataBase.shoplistDao().deleteShoplist(shoplistId)
            val checkDeletingResult =
                runCatching { appDataBase.shoplistDao().getShoplistById(shoplistId) }
            if (checkDeletingResult.isFailure) {
                return Result.success(Unit)
            } else {
                throw InvalidDatabaseStateException(message = "shoplist was not deleted")
            }
        }.onFailure { error ->
            if (BuildConfig.DEBUG) {
                error.printStackTrace()
            }
        }
        if (result.isFailure && retryNumber != 3) {
            result = deleteShoplist(
                shoplistId,
                retryNumber + 1
            )
        } else if (result.isFailure) {
            val otherOperationResult =
                runCatching { appDataBase.shoplistDao().getShoplistById(shoplistId) }
            if (otherOperationResult.isFailure) {
                Toast.makeText(
                    context,
                    "Техническая проблема с базой данных",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Техническая проблема с функцией удаления из базы данных",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return result
    }

    override suspend fun renameShoplist(
        shoplistId: Int,
        shoplistName: String,
        retryNumber: Int
    ): Result<Unit> {

        var result: Result<Unit> = runCatching {
            appDataBase.shoplistDao().renameShoplist(shoplistId, shoplistName)
            val shopList = appDataBase.shoplistDao().getShoplistById(shoplistId)
            if (shopList.shoplistName == shoplistName) {
                return Result.success(Unit)
            } else {
                throw InvalidDatabaseStateException(message = "invalid ssot, after rename name is not changed")
            }
        }.onFailure { error ->
            if (BuildConfig.DEBUG) {
                error.printStackTrace()
            }
        }
        if (result.isFailure && retryNumber != 3) {
            result = renameShoplist(
                shoplistId, shoplistName,
                retryNumber + 1
            )
        } else if (result.isFailure) {
            val otherOperationResult =
                runCatching { appDataBase.shoplistDao().getShoplistById(shoplistId) }
            if (otherOperationResult.isFailure) {
                Toast.makeText(
                    context,
                    "Техническая проблема с базой данных",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Техническая проблема с функцией переименования списка",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return result
    }

    override suspend fun doubleShoplist(shoplistId: Int, retryNumber: Int): Result<Unit> {

        var result: Result<Unit> = runCatching {
            val numberOfShoplistsBefore =
                appDataBase.shoplistDao().getShoplistByName(
                    appDataBase.shoplistDao().getShoplistById(shoplistId).shoplistName
                ).size
            appDataBase.shoplistDao()
                .insertShoplist(appDataBase.shoplistDao().getShoplistById(shoplistId))
            val numberOfShoplistsAfter =
                appDataBase.shoplistDao().getShoplistByName(
                    appDataBase.shoplistDao().getShoplistById(shoplistId).shoplistName
                ).size
            if (numberOfShoplistsAfter - numberOfShoplistsBefore == 1) {
                return Result.success(Unit)
            } else {
                throw InvalidDatabaseStateException(message = "shoplist was not doubled")
            }
        }.onFailure { error ->
            if (BuildConfig.DEBUG) {
                error.printStackTrace()
            }
        }
        if (result.isFailure && retryNumber != 3) {
            result = doubleShoplist(
                shoplistId,
                retryNumber + 1
            )
        } else if (result.isFailure) {
            val otherOperationResult =
                runCatching { appDataBase.shoplistDao().getShoplistById(shoplistId) }
            if (otherOperationResult.isFailure) {
                Toast.makeText(
                    context,
                    "Техническая проблема с базой данных",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Техническая проблема с функцией дублирования списка",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return result
    }

    private fun convertFromShoplistEntity(shoplists: List<ShoplistEntity>): List<Shoplist> {
        return shoplists.map { shoplist -> shoplistDbConvertor.map(shoplist) }
    }
}