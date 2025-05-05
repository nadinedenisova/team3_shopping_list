package com.example.my_shoplist_application.data.entity

import android.content.Context
import android.widget.Toast
import com.example.my_shoplist_application.BuildConfig
import com.example.my_shoplist_application.common.InvalidDatabaseStateException
import com.example.my_shoplist_application.data.convertors.IngredientsDbConvertor
import com.example.my_shoplist_application.data.convertors.ShoplistDbConvertor
import com.example.my_shoplist_application.db.AppDataBase
import com.example.my_shoplist_application.domain.db.ShoplistScreenRepository
import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.Shoplist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ShoplistScreenRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val shoplistDbConvertor: ShoplistDbConvertor,
    private val ingredientsDbConvertor: IngredientsDbConvertor,
    private val context: Context
) : ShoplistScreenRepository {

    override suspend fun createShoplist(
        shoplist: Shoplist, retryNumber: Int
    ): Result<Unit> {
        var result: Result<Unit> = runCatching {
            appDataBase.shoplistDao().insertShoplist(shoplistDbConvertor.map(shoplist))
            val checkAddingResult =
                runCatching { appDataBase.shoplistDao().getShoplistById(shoplist.id) }
            if (checkAddingResult.isFailure) {
                throw InvalidDatabaseStateException(message = "The shoplist was not added to DB")
            } else {
                return Result.success(Unit)
            }
        }.onFailure { error ->
            if (BuildConfig.DEBUG) {
                error.printStackTrace()
            }
        }
        if (result.isFailure && retryNumber != 3) {
            result = createShoplist(
                shoplist,
                retryNumber + 1
            )
        } else if (result.isFailure) {
            val otherOperationResult = runCatching { appDataBase.shoplistDao().getShoplists() }
            if (otherOperationResult.isFailure) {
                Toast.makeText(
                    context,
                    "Техническая проблема с базой данных",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Техническая проблема с функцией сохранения списка покупок в базу данных",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return result
    }

    override suspend fun saveIngredientToDB(
        ingredient: Ingredients,
        retryNumber: Int
    ): Result<Unit> {
        var result: Result<Unit> = runCatching {
            appDataBase.ingredientDao().insertIngredient(ingredientsDbConvertor.map(ingredient))
            val checkAddingResult =
                runCatching { appDataBase.ingredientDao().getIngredientById(ingredient.id) }
            if (checkAddingResult.isFailure) {
                throw InvalidDatabaseStateException(message = "The ingredient was not added to DB")
            } else {
                return Result.success(Unit)
            }
        }.onFailure { error ->
            if (BuildConfig.DEBUG) {
                error.printStackTrace()
            }
        }
        if (result.isFailure && retryNumber != 3) {
            result = saveIngredientToDB(
                ingredient,
                retryNumber + 1
            )
        } else if (result.isFailure) {
            val otherOperationResult = runCatching { appDataBase.ingredientDao().getIngredients() }
            if (otherOperationResult.isFailure) {
                Toast.makeText(
                    context,
                    "Техническая проблема с базой данных",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Техническая проблема с функцией добавления ингредиента в базу данных",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return result
    }

    override suspend fun saveIngredientToShoplist(
        ingredient: Ingredients,
        shoplist: Shoplist,
        retryNumber: Int
    ): Result<Unit> {
        var result: Result<Unit> = runCatching {
            val ingredients = shoplist.ingredientsIdList.toMutableList()
            ingredients.add(ingredient.id)
            appDataBase.shoplistDao()
                .insertIngredientInShoplist(shoplist.id, ingredients.joinToString(","))
            val checkAddingResult =
                runCatching {
                    appDataBase.shoplistDao().getShoplistIngredients(shoplist.id).split(",")
                        .map { it.trim().toInt() }
                        .contains(ingredient.id)
                }
            if (checkAddingResult.isFailure) {
                throw InvalidDatabaseStateException(message = "The ingredient was not added to shoplist")
            } else {
                return Result.success(Unit)
            }
        }.onFailure { error ->
            if (BuildConfig.DEBUG) {
                error.printStackTrace()
            }
        }
        if (result.isFailure && retryNumber != 3) {
            result = saveIngredientToDB(
                ingredient,
                retryNumber + 1
            )
        } else if (result.isFailure) {
            val otherOperationResult = runCatching { appDataBase.shoplistDao().getShoplists() }
            if (otherOperationResult.isFailure) {
                Toast.makeText(
                    context,
                    "Техническая проблема с базой данных",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Техническая проблема с функцией добавления ингредиента в список покупок",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return result
    }

    override suspend fun makeIngredientNotBought(
        ingredient: Ingredients,
        shoplist: Shoplist,
        retryNumber: Int
    ): Result<Unit> {
        var result: Result<Unit> = runCatching {
            appDataBase.ingredientDao().getIngredientById(ingredient.id).isBought = false
            val checkAddingResult =
                runCatching {
                    !appDataBase.ingredientDao().getIngredientById(ingredient.id).isBought
                }
            if (checkAddingResult.isFailure) {
                throw InvalidDatabaseStateException(message = "The ingredient was not made 'not bought'")
            } else {
                return Result.success(Unit)
            }
        }.onFailure { error ->
            if (BuildConfig.DEBUG) {
                error.printStackTrace()
            }
        }
        if (result.isFailure && retryNumber != 3) {
            result = makeIngredientNotBought(
                ingredient, shoplist,
                retryNumber + 1
            )
        } else if (result.isFailure) {
            val otherOperationResult = runCatching { appDataBase.shoplistDao().getShoplists() }
            if (otherOperationResult.isFailure) {
                Toast.makeText(
                    context,
                    "Техническая проблема с базой данных",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Техническая проблема с функцией удаления статуса ингредиента 'куплен'",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return result
    }

    override suspend fun makeIngredientBought(
        ingredient: Ingredients,
        shoplist: Shoplist,
        retryNumber: Int
    ): Result<Unit> {
        var result: Result<Unit> = runCatching {
            appDataBase.ingredientDao().getIngredientById(ingredient.id).isBought = true
            val checkAddingResult =
                runCatching {
                    appDataBase.ingredientDao().getIngredientById(ingredient.id).isBought
                }
            if (checkAddingResult.isFailure) {
                throw InvalidDatabaseStateException(message = "The ingredient was not made 'bought'")
            } else {
                return Result.success(Unit)
            }
        }.onFailure { error ->
            if (BuildConfig.DEBUG) {
                error.printStackTrace()
            }
        }
        if (result.isFailure && retryNumber != 3) {
            result = makeIngredientNotBought(
                ingredient, shoplist,
                retryNumber + 1
            )
        } else if (result.isFailure) {
            val otherOperationResult = runCatching { appDataBase.shoplistDao().getShoplists() }
            if (otherOperationResult.isFailure) {
                Toast.makeText(
                    context,
                    "Техническая проблема с базой данных",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Техническая проблема с функцией присвоения статуса ингредиенту 'куплен'",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return result
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
}
