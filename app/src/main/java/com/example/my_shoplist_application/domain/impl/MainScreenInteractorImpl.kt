package com.example.my_shoplist_application.domain.impl

import com.example.my_shoplist_application.domain.api.MainScreenInteractor
import com.example.my_shoplist_application.domain.db.MainScreenListError
import com.example.my_shoplist_application.domain.db.MainScreenRepository
import com.example.my_shoplist_application.domain.models.Shoplist
import com.example.my_shoplist_application.domain.db.Result
import kotlinx.coroutines.flow.Flow

class MainScreenInteractorImpl(private val mainScreenRepository: MainScreenRepository) :
    MainScreenInteractor {
    override suspend fun getShoplists(): Flow<Result<List<Shoplist>, MainScreenListError>> {
        return mainScreenRepository.getShoplists()
    }

    override suspend fun deleteShoplist(shoplistId: Int): kotlin.Result<Unit> {
        return mainScreenRepository.deleteShoplist(shoplistId)
    }

    override suspend fun renameShoplist(shoplistId: Int, shoplistName: String): kotlin.Result<Unit> {
        return mainScreenRepository.renameShoplist(shoplistId, shoplistName)
    }

    override suspend fun doubleShoplist(shoplistId: Int): kotlin.Result<Unit> {
        return mainScreenRepository.doubleShoplist(shoplistId)
    }

    override suspend fun onTogglePinList(shoplistId: Int): kotlin.Result<Unit> {
        return mainScreenRepository.onToggleShoplist(shoplistId)
    }
}