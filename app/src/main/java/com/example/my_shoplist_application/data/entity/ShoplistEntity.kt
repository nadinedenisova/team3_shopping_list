package com.example.my_shoplist_application.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.my_shoplist_application.domain.models.Ingridients

@Entity(tableName = "shoplist_table")
data class ShoplistEntity(
    @PrimaryKey(autoGenerate = true)
    val shoplistId: Int,
    @ColumnInfo(name = "shoplist_name")
    val shoplistName: String,
    @ColumnInfo(name = "shoplist_added_at")
    val addedAt: String,
    @ColumnInfo(name = "shoplist_ingridients_list")
    val ingridientsList: MutableList<Ingridients>,
    @ColumnInfo(name = "shoplist_is_pinned")
    var isPinned: Boolean
)
