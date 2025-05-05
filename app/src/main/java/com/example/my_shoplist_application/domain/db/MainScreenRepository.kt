package com.example.my_shoplist_application.domain.db

import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.flow.Flow

interface MainScreenRepository {
    suspend fun saveShopList(list:Shoplist): Long
    suspend fun getShoplistById(id: Int): Flow<Shoplist>
    suspend fun getShoplists(retryNumber: Int = 0): Flow<List<Shoplist>>
    suspend fun deleteShoplist(shoplistId: Int, retryNumber: Int = 0): Result<Unit>
    suspend fun renameShoplist(
        shoplistId: Int,
        shoplistName: String,
        retryNumber: Int = 0
    ): Result<Unit>

    suspend fun doubleShoplist(shoplistId: Int, retryNumber: Int = 0): Result<Unit>
}