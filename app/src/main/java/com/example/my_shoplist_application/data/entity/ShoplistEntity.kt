package com.example.my_shoplist_application.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.my_shoplist_application.domain.models.Ingridients

@Entity(tableName = "shoplist_table")
data class ShoplistEntity(
    @PrimaryKey(autoGenerate = true)
    val shoplistId: Int,
    val shoplistName: String,
    val addedAt: String,
    val ingridientsList: MutableList<Ingridients>,
    var isPinned: Boolean
)
