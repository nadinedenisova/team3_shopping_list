package com.example.my_shoplist_application.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "item_suggestions", primaryKeys = ["name"])
data class ItemSuggestionEntity(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "lastUsed")
    val lastUsed: Long
)