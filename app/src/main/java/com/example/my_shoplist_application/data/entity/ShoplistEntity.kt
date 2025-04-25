package com.example.my_shoplist_application.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.my_shoplist_application.domain.models.Ingredients

@Entity(tableName = "shoplist_table")
data class ShoplistEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shoplist_id")
    val shoplistId: Int,
    @ColumnInfo(name = "shoplist_name")
    val shoplistName: String,
    @ColumnInfo(name = "shoplist_added_at")
    val addedAt: String,
    @ColumnInfo(name = "shoplist_ingredients_list")
    val ingredientsList: MutableList<Ingredients>,
    @ColumnInfo(name = "shoplist_is_pinned")
    var isPinned: Boolean
)
