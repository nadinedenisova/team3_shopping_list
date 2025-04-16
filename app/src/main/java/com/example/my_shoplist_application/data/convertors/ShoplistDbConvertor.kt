package com.example.my_shoplist_application.data.convertors

import com.example.my_shoplist_application.data.entity.ShoplistEntity
import com.example.my_shoplist_application.domain.models.Shoplist

class ShoplistDbConvertor {

    fun map(shoplist: Shoplist): ShoplistEntity {
        return ShoplistEntity(
            shoplist.id,
            shoplist.shoplistName,
            System.currentTimeMillis().toString(),
            shoplist.ingridientsList
        )
    }

    fun map(shoplist: ShoplistEntity): Shoplist {
        return Shoplist(
            shoplist.shoplistId,
            shoplist.shoplistName,
            shoplist.ingridientsList
        )
    }
}