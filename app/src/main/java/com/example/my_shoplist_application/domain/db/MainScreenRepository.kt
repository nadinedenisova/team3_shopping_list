package com.example.my_shoplist_application.domain.db

import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.flow.Flow

interface MainScreenRepository {
    suspend fun getShoplists(): Flow<List<Shoplist>>
    suspend fun deleteShoplist(shoplistId: Int)
    suspend fun renameShoplist(shoplistId: Int, shoplistName: String)
    suspend fun doubleShoplist(shoplistId: Int)
}