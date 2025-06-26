package com.example.my_shoplist_application.data.convertors

import com.example.my_shoplist_application.data.entity.ShoplistEntity
import com.example.my_shoplist_application.domain.models.Shoplist

class ShoplistDbConvertor {

    fun map(shoplist: Shoplist): ShoplistEntity {
        return ShoplistEntity(
            shoplist.id,
            shoplist.shoplistName,
            System.currentTimeMillis().toString(),
            shoplist.ingredientsIdList.joinToString(","),
            shoplist.isPinned,
            shoplist.isSelectProducts
        )
    }

    fun map(shoplist: ShoplistEntity): Shoplist {
        val ingredientsList = if (shoplist.ingredientIdsList.isEmpty()) emptyList()
        else {
            shoplist.ingredientIdsList.split(",").map { it.trim().toInt() }
        }
        return Shoplist(
            shoplist.shoplistId,
            shoplist.shoplistName,
            ingredientsList,
            shoplist.isPinned,
            shoplist.isSelectProducts
        )
    }
}
