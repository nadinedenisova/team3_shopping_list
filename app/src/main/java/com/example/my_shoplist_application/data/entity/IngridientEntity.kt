package com.example.my_shoplist_application.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingridient_table")
data class IngridientEntity(
    @PrimaryKey(autoGenerate = true)
    val indridientId: Int,
    val ingridientName: String,
    val ingridientQuantity: Int,
    val ingridientUnit: String,
    var isBought: Boolean
)
