package com.example.my_shoplist_application.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.my_shoplist_application.data.entity.IngredientEntity

@Dao
interface IngredientsDao {

    @Upsert
    fun insertIngredient(ingredient: IngredientEntity)

    @Query("SELECT ingredientId, ingredient_name, ingredient_quantity, ingredient_unit, ingredient_is_bought FROM ingredient_table")
    suspend fun getIngredients(): List<IngredientEntity>

}