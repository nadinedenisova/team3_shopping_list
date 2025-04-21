package com.example.my_shoplist_application.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingridient_table")
data class IngridientEntity(
    @PrimaryKey(autoGenerate = true)
    val indridientId: Int,
    @ColumnInfo(name = "ingridient_name")
    val ingridientName: String,
    @ColumnInfo(name = "ingridient_quantity")
    val ingridientQuantity: Int,
    @ColumnInfo(name = "ingridient_unit")
    val ingridientUnit: String,
    @ColumnInfo(name = "ingridient_is_bought")
    var isBought: Boolean
)
