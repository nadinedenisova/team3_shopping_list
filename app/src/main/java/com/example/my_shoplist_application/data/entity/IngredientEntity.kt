package com.example.my_shoplist_application.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.my_shoplist_application.domain.models.MeasurementUnit

@Entity(
    tableName = "ingredient_table",
    foreignKeys = [ForeignKey(  // если удалить список, удалятся все ингридиенты этого списка из DataBase
        entity = ShoplistEntity::class,
        parentColumns = ["shoplist_id"],
        childColumns = ["listId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ingredient_id")
    val ingredientId: Int,
    @ColumnInfo(name = "ingredient_name")
    val ingredientName: String,
    @ColumnInfo(name = "ingredient_quantity")
    val ingredientQuantity: Int,
    @ColumnInfo(name = "ingredient_unit")
    val ingredientUnit: MeasurementUnit,
    @ColumnInfo(name = "listId")
    val shopListId: Int,
    @ColumnInfo(name = "ingredient_is_bought")
    var isBought: Boolean
)

