package com.example.my_shoplist_application.domain.api

import com.example.my_shoplist_application.domain.db.MainScreenListError
import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.flow.Flow

interface MainScreenInteractor {
    suspend fun getShoplists(): Flow<com.example.my_shoplist_application.domain.db.Result<List<Shoplist>, MainScreenListError>>
    suspend fun deleteShoplist(shoplistId: Int): Result<Unit>
    suspend fun renameShoplist(shoplistId: Int, shoplistName: String): Result<Unit>
    suspend fun doubleShoplist(shoplistId: Int): Result<Unit>
    suspend fun onTogglePinList(shoplistId: Int): Result<Unit>
    suspend fun getShoplist(): Flow<List<Shoplist>>
    suspend fun getShoplistById(id: Int): Flow <Shoplist?>
    suspend fun saveShopList(list:Shoplist): Long
}