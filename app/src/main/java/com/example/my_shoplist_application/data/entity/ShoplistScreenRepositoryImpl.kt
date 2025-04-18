package com.example.my_shoplist_application.data.entity

import android.content.Context
import com.example.my_shoplist_application.data.convertors.IngridientsDbConvertor
import com.example.my_shoplist_application.data.convertors.ShoplistDbConvertor
import com.example.my_shoplist_application.db.AppDataBase
import com.example.my_shoplist_application.domain.db.ShoplistScreenRepository
import com.example.my_shoplist_application.domain.models.Shoplist

class ShoplistScreenRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val shoplistDbConvertor: ShoplistDbConvertor,
    private val ingridientsDbConvertor: IngridientsDbConvertor,
    private val context: Context
) : ShoplistScreenRepository {

    override fun createShoplist(shoplist: Shoplist) {
        appDataBase.shoplistDao().insertShoplist(shoplistDbConvertor.map(shoplist))
    }
}
