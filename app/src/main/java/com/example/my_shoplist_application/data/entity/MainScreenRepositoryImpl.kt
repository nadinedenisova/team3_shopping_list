package com.example.my_shoplist_application.data.entity

import android.content.Context
import android.widget.Toast
import com.example.my_shoplist_application.data.convertors.ShoplistDbConvertor
import com.example.my_shoplist_application.data.dao.ShoplistDao
import com.example.my_shoplist_application.domain.db.MainScreenRepository
import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainScreenRepositoryImpl(
    private val shoplistDao: ShoplistDao,
    private val shoplistDbConvertor: ShoplistDbConvertor,
    private val context: Context
) : MainScreenRepository {

    override suspend fun getShoplists(): Flow<List<Shoplist>> = flow {
        try {
            val shoplists = shoplistDao.getShoplists()
            emit(convertFromShoplistEntity(shoplists))
        } catch (e: Exception) {
            delay(1000)
            try {
                val shoplists = shoplistDao.getShoplists()
                emit(convertFromShoplistEntity(shoplists))
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override suspend fun deleteShoplist(shoplistId: Int) {
        return shoplistDao.deleteShoplist(shoplistId)
    }

    override suspend fun renameShoplist(shoplistId: Int, shoplistName: String) {
        shoplistDao.renameShoplist(shoplistId, shoplistName)
    }

    override suspend fun doubleShoplist(shoplistId: Int) {
        shoplistDao
            .insertShoplist(shoplistDao.getShoplistById(shoplistId))

    }

    private fun convertFromShoplistEntity(shoplists: List<ShoplistEntity>): List<Shoplist> {
        return shoplists.map { shoplist -> shoplistDbConvertor.map(shoplist) }
    }
}