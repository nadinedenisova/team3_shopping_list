package com.example.my_shoplist_application.domain.impl

import com.example.my_shoplist_application.domain.api.MainScreenInteractor
import com.example.my_shoplist_application.domain.db.MainScreenRepository
import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.flow.Flow

class MainScreenInteractorImpl(private val mainScreenRepository: MainScreenRepository) :
    MainScreenInteractor {
    override suspend fun getShoplists(): Flow<List<Shoplist>> {
        return mainScreenRepository.getShoplists()
    }

    override suspend fun deleteShoplist(shoplistId: Int) {
        return mainScreenRepository.deleteShoplist(shoplistId)
    }

    override suspend fun renameShoplist(shoplistId: Int, shoplistName: String) {
        return mainScreenRepository.renameShoplist(shoplistId, shoplistName)
    }

    override suspend fun doubleShoplist(shoplistId: Int) {
        return mainScreenRepository.doubleShoplist(shoplistId)
    }
}