package com.example.my_shoplist_application.domain.impl

import com.example.my_shoplist_application.domain.api.ShoplistScreenInteractor
import com.example.my_shoplist_application.domain.db.ShoplistScreenRepository
import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.flow.Flow

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

    override suspend fun updateItem(ingredient: Ingredients) {
        shoplistScreenRepository.updateItem(ingredient)
    }

    override suspend fun deleteIngredient(ingredient: Ingredients) {
        shoplistScreenRepository.deleteIngredient(ingredient)
    }

    override suspend fun getIngredients(listId: Int): Flow<List<Ingredients>> {
        return shoplistScreenRepository.getIngredients(listId)
    }

    override suspend fun getSuggestions(): Flow<List<String>> {
      return  shoplistScreenRepository.getSuggestions()
    }

    override suspend fun saveSuggestion(name: String) {
        shoplistScreenRepository.saveSuggestion(name)
    }

    override suspend fun getSuggestionsByPrefix(prefix: String): List<String> {
        return shoplistScreenRepository.getSuggestionsByPrefix(prefix)
    }
}