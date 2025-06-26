package com.example.my_shoplist_application.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val ingredientIdsList: String,
    @ColumnInfo(name = "shoplist_is_pinned")
    var isPinned: Boolean,
    @ColumnInfo(name = "shoplist_is_select_products")
    var isSelectProducts: Boolean
)

