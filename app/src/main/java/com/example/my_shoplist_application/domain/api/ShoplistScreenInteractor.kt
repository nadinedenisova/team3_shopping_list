package com.example.my_shoplist_application.domain.api

import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.Shoplist

interface ShoplistScreenInteractor {
    suspend fun createShoplist(shoplist: Shoplist): Result<Unit>
    suspend fun saveIngredientToDB(ingredient: Ingredients): Result<Unit>
    suspend fun saveIngredientToShoplist(ingredient: Ingredients, shoplist: Shoplist): Result<Unit>
    suspend fun makeIngredientNotBought(ingredient: Ingredients, shoplist: Shoplist): Result<Unit>
    suspend fun makeIngredientBought(ingredient: Ingredients, shoplist: Shoplist): Result<Unit>

}