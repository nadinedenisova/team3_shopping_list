package com.example.my_shoplist_application.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient_table")
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Int,
    @ColumnInfo(name = "ingredient_name")
    val ingredientName: String,
    @ColumnInfo(name = "ingredient_quantity")
    val ingredientQuantity: Int,
    @ColumnInfo(name = "ingredient_unit")
    val ingredientUnit: String,
    @ColumnInfo(name = "ingredient_is_bought")
    var isBought: Boolean
)
