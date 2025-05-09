package com.example.my_shoplist_application.domain.api

import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.flow.Flow

interface ShoplistScreenInteractor {
    suspend fun createShoplist(shoplist: Shoplist): Result<Unit>
    suspend fun saveIngredientToDB(ingredient: Ingredients): Result<Unit>
    suspend fun saveIngredientToShoplist(ingredient: Ingredients, shoplist: Shoplist): Result<Unit>
    suspend fun makeIngredientNotBought(ingredient: Ingredients, shoplist: Shoplist): Result<Unit>
    suspend fun makeIngredientBought(ingredient: Ingredients, shoplist: Shoplist): Result<Unit>
    suspend fun updateItem(ingredient: Ingredients)
    suspend fun deleteIngredient(ingredient: Ingredients)
    suspend fun getIngredients(listId: Int): Flow<List<Ingredients>>
    suspend fun getSuggestions(): Flow<List<String>>
    suspend fun saveSuggestion(name: String)
    suspend fun getSuggestionsByPrefix(prefix: String): List<String>
    suspend fun deleteBoughtItems()
    suspend fun updateAllBoughtStatus(listid: Int, isBought: Boolean):Flow<List<Ingredients>>
}

