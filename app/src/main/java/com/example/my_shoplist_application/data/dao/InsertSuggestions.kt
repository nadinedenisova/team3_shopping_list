package com.example.my_shoplist_application.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_shoplist_application.data.entity.ItemSuggestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InsertSuggestions {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestion: ItemSuggestionEntity)

    @Query("SELECT name FROM item_suggestions ORDER BY lastUsed DESC LIMIT 20")
    fun getSuggestions(): Flow<List<String>>

    @Query("SELECT name FROM item_suggestions WHERE name LIKE :prefix || '%' ORDER BY lastUsed DESC LIMIT 10")
    suspend fun getSuggestionsByPrefix(prefix: String): List<String>
}