package com.example.my_shoplist_application.domain.impl

import com.example.my_shoplist_application.domain.api.ShoplistScreenInteractor
import com.example.my_shoplist_application.domain.db.ShoplistScreenRepository

class ShoplistScreenInteractorImpl(private val shoplistScreenRepository: ShoplistScreenRepository) :
    ShoplistScreenInteractor {
}