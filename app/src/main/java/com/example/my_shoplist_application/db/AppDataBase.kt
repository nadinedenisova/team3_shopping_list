package com.example.my_shoplist_application.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.my_shoplist_application.data.dao.IngridientsDao
import com.example.my_shoplist_application.data.dao.ShoplistDao
import com.example.my_shoplist_application.data.entity.IngridientEntity
import com.example.my_shoplist_application.data.entity.ShoplistEntity

@Database(
    version = 1,
    entities = [ShoplistEntity::class, IngridientEntity::class]
)

abstract class AppDataBase : RoomDatabase() {
    abstract fun shoplistDao(): ShoplistDao
    abstract fun ingridientDao(): IngridientsDao
}