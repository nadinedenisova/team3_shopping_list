package com.example.my_shoplist_application.domain.db

import com.example.my_shoplist_application.domain.models.Shoplist

interface ShoplistScreenRepository {
    fun createShoplist(shoplist: Shoplist)
}