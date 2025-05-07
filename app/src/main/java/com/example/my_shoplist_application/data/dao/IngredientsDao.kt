package com.example.my_shoplist_application.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.my_shoplist_application.data.entity.IngredientEntity
import com.example.my_shoplist_application.data.entity.ItemSuggestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientsDao {

    @Upsert
    fun insertIngredient(ingredient: IngredientEntity)

    @Query("SELECT ingredient_id, ingredient_name, ingredient_quantity, ingredient_unit, listId, ingredient_is_bought FROM ingredient_table")
    suspend fun getIngredients(): List<IngredientEntity>

    @Query("SELECT ingredient_id, ingredient_name, ingredient_quantity, ingredient_unit, listId, ingredient_is_bought FROM ingredient_table WHERE ingredient_id = :ingredientId")
    suspend fun getIngredientById(ingredientId: Int): IngredientEntity

    @Query("UPDATE ingredient_table SET ingredient_is_bought = 1 WHERE ingredient_id = :ingredientId")
    fun makeIngredientBought(ingredientId: Int)

    @Query("UPDATE ingredient_table SET ingredient_is_bought = 0 WHERE ingredient_id = :ingredientId")
    fun makeIngredientNotBought(ingredientId: Int)

    @Query("SELECT COUNT(*) > 0 FROM ingredient_table WHERE ingredient_id = :ingredientId AND ingredient_is_bought = 1")
    suspend fun isIngredientBought(ingredientId: Int): Boolean

    @Update
    suspend fun updateItem(item: IngredientEntity)

    @Query("SELECT * FROM ingredient_table WHERE listId = :listId")
    fun getIngredientsListId(listId: Int): Flow<List<IngredientEntity>>

    @Delete
    suspend fun deleteIngredient(ingredient: IngredientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)// это для подсказки при вводе текста
    suspend fun insertSuggestion(suggestion: ItemSuggestionEntity)

    @Query("DELETE FROM ingredient_table WHERE ingredient_is_bought = 1")
    suspend fun deleteBoughtItems()

    @Query("UPDATE ingredient_table SET ingredient_is_bought = :isBought WHERE listId = :listid")
    suspend fun updateAllBoughtStatus(listid: Int, isBought: Boolean)
}