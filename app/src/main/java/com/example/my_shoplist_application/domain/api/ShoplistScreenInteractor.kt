package com.example.my_shoplist_application.domain.api

import com.example.my_shoplist_application.domain.models.Shoplist

interface ShoplistScreenInteractor {
    fun createShoplist(shoplist: Shoplist)
}