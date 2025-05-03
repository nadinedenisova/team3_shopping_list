package com.example.my_shoplist_application.domain.db

import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.Shoplist

interface ShoplistScreenRepository {
    suspend fun createShoplist(shoplist: Shoplist, retryNumber: Int = 0): kotlin.Result<Unit>
    suspend fun saveIngredientToDB(ingredient: Ingredients, retryNumber: Int = 0): kotlin.Result<Unit>

    suspend fun saveIngredientToShoplist(
        ingredient: Ingredients, shoplist: Shoplist,
        retryNumber: Int = 0
    ): kotlin.Result<Unit>

    suspend fun makeIngredientNotBought(
        ingredient: Ingredients,
        shoplist: Shoplist,
        retryNumber: Int = 0
    ): kotlin.Result<Unit>

    suspend fun makeIngredientBought(
        ingredient: Ingredients,
        shoplist: Shoplist,
        retryNumber: Int = 0
    ): kotlin.Result<Unit>


}