package com.example.my_shoplist_application.domain.db

import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.flow.Flow

interface ShoplistScreenRepository {
    suspend fun createShoplist(shoplist: Shoplist, retryNumber: Int = 0): Result<Unit>
    suspend fun saveIngredientToDB(ingredient: Ingredients, retryNumber: Int = 0): Result<Unit>

    suspend fun saveIngredientToShoplist(
        ingredient: Ingredients, shoplist: Shoplist,
        retryNumber: Int = 0
    ): Result<Unit>

    suspend fun makeIngredientNotBought(
        ingredient: Ingredients,
        shoplist: Shoplist,
        retryNumber: Int = 0
    ): Result<Unit>

    suspend fun makeIngredientBought(
        ingredient: Ingredients,
        shoplist: Shoplist,
        retryNumber: Int = 0
    ): Result<Unit>

    suspend fun updateItem(ingredient: Ingredients)

    suspend fun deleteIngredient(ingredient: Ingredients)

    suspend fun getIngredients(listId: Int): Flow<List<Ingredients>>

    suspend fun getSuggestions(): Flow<List<String>>

    suspend fun saveSuggestion(name: String)

    suspend fun getSuggestionsByPrefix(prefix: String): List<String>
}