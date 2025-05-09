package com.example.my_shoplist_application.domain.impl

import com.example.my_shoplist_application.domain.api.ShoplistScreenInteractor
import com.example.my_shoplist_application.domain.db.ShoplistScreenRepository
import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.Shoplist

class ShoplistScreenInteractorImpl(private val shoplistScreenRepository: ShoplistScreenRepository) :
    ShoplistScreenInteractor {
    override suspend fun createShoplist(shoplist: Shoplist): Result<Unit> {
        return shoplistScreenRepository.createShoplist(shoplist)
    }

    override suspend fun saveIngredientToDB(ingredient: Ingredients): Result<Unit> {
        return shoplistScreenRepository.saveIngredientToDB(ingredient)
    }

    override suspend fun saveIngredientToShoplist(
        ingredient: Ingredients,
        shoplist: Shoplist
    ): Result<Unit> {
        return shoplistScreenRepository.saveIngredientToShoplist(ingredient, shoplist)
    }

    override suspend fun makeIngredientNotBought(
        ingredient: Ingredients,
        shoplist: Shoplist
    ): Result<Unit> {
        return shoplistScreenRepository.makeIngredientNotBought(ingredient, shoplist)
    }

    override suspend fun makeIngredientBought(
        ingredient: Ingredients,
        shoplist: Shoplist
    ): Result<Unit> {
        return shoplistScreenRepository.makeIngredientBought(ingredient, shoplist)
    }
}