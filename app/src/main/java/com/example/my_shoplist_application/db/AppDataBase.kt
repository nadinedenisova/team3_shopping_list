package com.example.my_shoplist_application.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.my_shoplist_application.data.dao.IngredientsDao
import com.example.my_shoplist_application.data.dao.InsertSuggestions
import com.example.my_shoplist_application.data.dao.ShoplistDao
import com.example.my_shoplist_application.data.entity.IngredientEntity
import com.example.my_shoplist_application.data.entity.ItemSuggestionEntity
import com.example.my_shoplist_application.data.entity.ShoplistEntity

@Database(
    version = 1,
    entities = [ShoplistEntity::class, IngredientEntity::class, ItemSuggestionEntity::class]
)

abstract class AppDataBase : RoomDatabase() {
    abstract fun shoplistDao(): ShoplistDao
    abstract fun ingredientDao(): IngredientsDao
    abstract fun insertSuggestion(): InsertSuggestions
}
