package com.example.my_shoplist_application.data.entity

import com.example.my_shoplist_application.BuildConfig
import com.example.my_shoplist_application.data.convertors.IngredientsDbConvertor
import com.example.my_shoplist_application.data.convertors.ShoplistDbConvertor
import com.example.my_shoplist_application.db.AppDataBase
import com.example.my_shoplist_application.domain.db.ShoplistScreenRepository
import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.MeasurementUnit
import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException

class ShoplistScreenRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val shoplistDbConvertor: ShoplistDbConvertor,
    private val ingredientsDbConvertor: IngredientsDbConvertor
) : ShoplistScreenRepository {

    private suspend fun interactWithDb(
        shoplistId: Int = 0,
        choice: Int,
        shoplist: Shoplist = Shoplist(0, "", emptyList()),
        ingredient: Ingredients = Ingredients(0, "", 0F, MeasurementUnit.PCS, 0, false),
        shoplistName: String = "",
        retryNumber: Int = 0,

        ): Result<Unit> {
        val result = runCatching {
            when (choice) {
                1 -> appDataBase.shoplistDao().insertShoplist(shoplistDbConvertor.map(shoplist))
                2 -> appDataBase.ingredientDao()
                    .insertIngredient(ingredientsDbConvertor.map(ingredient))

                3 -> {
                    val ingredients = shoplist.ingredientsIdList.toMutableList()
                    ingredients.add(ingredient.id)
                    appDataBase.shoplistDao()
                        .insertIngredientInShoplist(shoplist.id, ingredients.joinToString(","))
                }

                4 -> appDataBase.ingredientDao().getIngredientById(ingredient.id).isBought = false
                5 -> appDataBase.ingredientDao().getIngredientById(ingredient.id).isBought = true
            }
        }
        if (result.isSuccess) return result

        return if (retryNumber != 3) {
            interactWithDb(shoplistId, choice, shoplist, ingredient, shoplistName, retryNumber + 1)
        } else {
            result
        }
    }

    override suspend fun createShoplist(
        shoplist: Shoplist, retryNumber: Int
    ): Result<Unit> {
        return interactWithDb(shoplist = shoplist, choice = 1)
            .onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    error.printStackTrace()
                }
            }
    }

    override suspend fun saveIngredientToDB(
        ingredient: Ingredients,
        retryNumber: Int
    ): Result<Unit> {
        return interactWithDb(ingredient = ingredient, choice = 2)
            .onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    error.printStackTrace()
                }
            }
    }

    override suspend fun saveIngredientToShoplist(
        ingredient: Ingredients,
        shoplist: Shoplist,
        retryNumber: Int
    ): Result<Unit> {
        return interactWithDb(ingredient = ingredient, shoplist = shoplist, choice = 3)
            .onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    error.printStackTrace()
                }
            }
    }

    override suspend fun makeIngredientNotBought(
        ingredient: Ingredients,
        shoplist: Shoplist,
        retryNumber: Int
    ): Result<Unit> {
        return interactWithDb(ingredient = ingredient, shoplist = shoplist, choice = 4)
            .onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    error.printStackTrace()
                }
            }
    }

    override suspend fun makeIngredientBought(
        ingredient: Ingredients,
        shoplist: Shoplist,
        retryNumber: Int
    ): Result<Unit> {
        return interactWithDb(ingredient = ingredient, shoplist = shoplist, choice = 5)
            .onFailure { error ->
                if (error is CancellationException) {
                    throw CancellationException()
                }
                if (BuildConfig.DEBUG) {
                    error.printStackTrace()
                }
            }
    }

    override suspend fun updateItem(ingredient: Ingredients) {
        appDataBase.ingredientDao().updateItem(ingredientsDbConvertor.map(ingredient))
    }

    override suspend fun deleteIngredient(ingredient: Ingredients) {
        appDataBase.ingredientDao().deleteIngredient(ingredientsDbConvertor.map(ingredient))
    }

    override suspend fun getIngredients(listid: Int): Flow<List<Ingredients>> {
        val ingredientEntity =
            appDataBase.ingredientDao().getIngredientsListId(listid)
                .map { igrif -> igrif.map { ingredientsDbConvertor.map(it) } }
        return ingredientEntity
    }


    override suspend fun getSuggestions(): Flow<List<String>> {
        return appDataBase.insertSuggestion().getSuggestions()
    }

    override suspend fun saveSuggestion(name: String) {
        appDataBase.insertSuggestion().insertSuggestion(
            ItemSuggestionEntity(
                name = name,
                lastUsed = System.currentTimeMillis()
            )
        )
    }

    override suspend fun getSuggestionsByPrefix(prefix: String): List<String> {
        return appDataBase.insertSuggestion().getSuggestionsByPrefix(prefix)
    }

    override suspend fun deleteBoughtItems() {
        appDataBase.ingredientDao().deleteBoughtItems()
    }
}
