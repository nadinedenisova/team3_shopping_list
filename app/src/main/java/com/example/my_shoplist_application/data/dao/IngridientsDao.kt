package com.example.my_shoplist_application.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_shoplist_application.data.entity.IngridientEntity

@Dao
interface IngridientsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngridient(ingridient: IngridientEntity)

    @Query("SELECT * FROM ingridient_table")
    suspend fun getIngridients(): List<IngridientEntity>

}