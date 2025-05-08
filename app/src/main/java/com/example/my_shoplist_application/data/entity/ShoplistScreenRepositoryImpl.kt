package com.example.my_shoplist_application.data.entity

import com.example.my_shoplist_application.BuildConfig
import com.example.my_shoplist_application.data.convertors.IngredientsDbConvertor
import com.example.my_shoplist_application.data.convertors.ShoplistDbConvertor
import com.example.my_shoplist_application.data.sharedManager.SharedManager
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
    private val ingredientsDbConvertor: IngredientsDbConvertor,
    private val sharedManager: SharedManager
) : ShoplistScreenRepository {

    private suspend fun interactWithDb(
        shoplistId: Int = 0,
        choice: Int,
        shoplist: Shoplist = Shoplist(0, "", emptyList()),
        ingredient: Ingredients = Ingredients(0, "", 0, MeasurementUnit.PCS, 0, false),
        retryNumber: Int = 0,

        ): Result<Unit> {
        val result = runCatching {
            when (choice) {
                CREATE_CHOICE -> appDataBase.shoplistDao()
                    .insertShoplist(shoplistDbConvertor.map(shoplist))

                SAVE_INGREDIENT_TO_DB_CHOICE -> appDataBase.ingredientDao()
                    .insertIngredient(ingredientsDbConvertor.map(ingredient))

                SAVE_INGREDIENT_TO_SHOPLIST_CHOICE -> {
                    val ingredients = shoplist.ingredientsIdList.toMutableList()
                    ingredients.add(ingredient.id)
                    appDataBase.shoplistDao()
                        .insertIngredientInShoplist(shoplist.id, ingredients.joinToString(","))
                }

                MAKE_BOUGHT_CHOICE -> appDataBase.ingredientDao()
                    .getIngredientById(ingredient.id).isBought = false

                MAKE_NOT_BOUGHT_CHOICE -> appDataBase.ingredientDao()
                    .getIngredientById(ingredient.id).isBought = true

                else -> {}
            }
            Unit
        }
        if (result.isSuccess) return result

        return if (retryNumber != MAX_RETRY_NUMBER) {
            interactWithDb(shoplistId, choice, shoplist, ingredient, retryNumber + 1)
        } else {
            result
        }
    }

    override suspend fun createShoplist(
        shoplist: Shoplist, retryNumber: Int
    ): Result<Unit> {
        return interactWithDb(shoplist = shoplist, choice = CREATE_CHOICE)
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
        return interactWithDb(ingredient = ingredient, choice = SAVE_INGREDIENT_TO_DB_CHOICE)
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
        return interactWithDb(
            ingredient = ingredient,
            shoplist = shoplist,
            choice = SAVE_INGREDIENT_TO_SHOPLIST_CHOICE
        )
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
        return interactWithDb(
            ingredient = ingredient,
            shoplist = shoplist,
            choice = MAKE_BOUGHT_CHOICE
        )
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
        return interactWithDb(
            ingredient = ingredient,
            shoplist = shoplist,
            choice = MAKE_NOT_BOUGHT_CHOICE
        )
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

    override suspend fun getIngredients(listId: Int): Flow<List<Ingredients>> {
        val ingredientEntity =
            appDataBase.ingredientDao().getIngredientsListId(listId)
                .map { item -> item.map { ingredientsDbConvertor.map(it) } }
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

    override suspend fun updateAllBoughtStatus(listid: Int, isBought: Boolean): Flow<List<Ingredients>> {
        appDataBase.ingredientDao().updateAllBoughtStatus(listid,isBought)
        val list = appDataBase.ingredientDao().getIngredientsListId(listid).map { it -> it.map { ingredientsDbConvertor.map(it) } }
        return list
    }

    override fun switchIsChecked(isChecked: Boolean) {
        sharedManager.putSwitchStatus(isChecked)
    }

    override fun getSwitchStatus(): Boolean {
        return sharedManager.getSwitchStatus()
    }

    companion object {
        private const val CREATE_CHOICE = 1
        private const val SAVE_INGREDIENT_TO_DB_CHOICE = 2
        private const val SAVE_INGREDIENT_TO_SHOPLIST_CHOICE = 3
        private const val MAKE_BOUGHT_CHOICE = 4
        private const val MAKE_NOT_BOUGHT_CHOICE = 5
        private const val MAX_RETRY_NUMBER = 3
    }
}

