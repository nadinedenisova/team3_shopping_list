package com.example.my_shoplist_application.domain.impl

import com.example.my_shoplist_application.domain.api.ShoplistScreenInteractor
import com.example.my_shoplist_application.domain.db.ShoplistScreenRepository
import com.example.my_shoplist_application.domain.models.Shoplist

class ShoplistScreenInteractorImpl(private val shoplistScreenRepository: ShoplistScreenRepository) :
    ShoplistScreenInteractor {
    override fun createShoplist(shoplist: Shoplist) {
        return shoplistScreenRepository.createShoplist(shoplist)
    }
}