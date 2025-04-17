package com.example.my_shoplist_application.data.entity

import android.content.Context
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

    override suspend fun getShoplists(): Flow<List<Shoplist>> = flow {
        val shoplists = appDataBase.shoplistDao().getShoplists()
        emit(convertFromShoplistEntity(shoplists))
    }

    override suspend fun deleteShoplist(shoplistId: Int) {
        appDataBase.shoplistDao().deleteShoplist(shoplistId)
    }

    override suspend fun renameShoplist(shoplistId: Int, shoplistName: String) {
        appDataBase.shoplistDao().renameShoplist(shoplistId, shoplistName)
    }

    override suspend fun doubleShoplist(shoplistId: Int) {
        appDataBase.shoplistDao()
            .insertShoplist(appDataBase.shoplistDao().getShoplistById(shoplistId))

    }

    private fun convertFromShoplistEntity(shoplists: List<ShoplistEntity>): List<Shoplist> {
        return shoplists.map { shoplist -> shoplistDbConvertor.map(shoplist) }
    }
}