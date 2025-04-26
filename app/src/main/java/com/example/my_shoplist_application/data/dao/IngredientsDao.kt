package com.example.my_shoplist_application.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.my_shoplist_application.data.entity.IngredientEntity

@Dao
interface IngredientsDao {

    @Upsert
    fun insertIngredient(ingredient: IngredientEntity)

    @Query("SELECT ingredient_id, ingredient_name, ingredient_quantity, ingredient_unit, ingredient_is_bought FROM ingredient_table")
    suspend fun getIngredients(): List<IngredientEntity>

    @Query("SELECT ingredient_id, ingredient_name, ingredient_quantity, ingredient_unit, ingredient_is_bought FROM ingredient_table WHERE ingredient_id = :ingredientId")
    suspend fun getIngredientById(ingredientId: Int): IngredientEntity

    @Query("UPDATE ingredient_table SET ingredient_is_bought = 1 WHERE ingredient_id = :ingredientId")
    fun makeIngredientBought(ingredientId: Int)

    @Query("UPDATE ingredient_table SET ingredient_is_bought = 0 WHERE ingredient_id = :ingredientId")
    fun makeIngredientNotBought(ingredientId: Int)

    @Query("SELECT COUNT(*) > 0 FROM ingredient_table WHERE ingredient_id = :ingredientId AND ingredient_is_bought = 1")
    suspend fun isIngredientBought(ingredientId: Int): Boolean

}