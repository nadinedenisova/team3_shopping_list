package com.example.my_shoplist_application.domain.db

import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.flow.Flow

interface MainScreenRepository {
    suspend fun getShoplists(retryNumber: Int = 0): Flow<Result<List<Shoplist>, MainScreenListError>>
    suspend fun deleteShoplist(shoplistId: Int, retryNumber: Int = 0): kotlin.Result<Unit>
    suspend fun renameShoplist(
        shoplistId: Int,
        shoplistName: String,
        retryNumber: Int = 0
    ): kotlin.Result<Unit>

    suspend fun doubleShoplist(shoplistId: Int, retryNumber: Int = 0): kotlin.Result<Unit>
    suspend fun onToggleShoplist(shoplistId: Int, retryNumber: Int = 0): kotlin.Result<Unit>
}