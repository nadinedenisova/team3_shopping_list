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
                Toast.makeText(
                    context,
                    "Техническая проблема с функцией добавления ингредиента в базу данных",
                    Toast.LENGTH_SHORT
                ).show()
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
            val oldListOfIngredients: MutableList<Ingredients> =
                appDataBase.shoplistDao().getShoplistIngredients(shoplist.id).toMutableList()
            oldListOfIngredients.add(ingredient)
            appDataBase.shoplistDao().insertIngredientInShoplist(shoplist.id, oldListOfIngredients)
            val checkAddingResult =
                runCatching {
                    appDataBase.shoplistDao().getShoplistIngredients(shoplist.id)
                        .contains(ingredient)
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
            val listOfIngredients: MutableList<Ingredients> =
                appDataBase.shoplistDao().getShoplistIngredients(shoplist.id).toMutableList()
            listOfIngredients[listOfIngredients.indexOf(ingredient)].isBought = false
            val checkAddingResult =
                runCatching {
                    !appDataBase.shoplistDao()
                        .getShoplistIngredients(shoplist.id)[listOfIngredients.indexOf(ingredient)].isBought
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
            val listOfIngredients: MutableList<Ingredients> =
                appDataBase.shoplistDao().getShoplistIngredients(shoplist.id).toMutableList()
            listOfIngredients[listOfIngredients.indexOf(ingredient)].isBought = true
            val checkAddingResult =
                runCatching {
                    appDataBase.shoplistDao()
                        .getShoplistIngredients(shoplist.id)[listOfIngredients.indexOf(
                        ingredient
                    )].isBought
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
}
