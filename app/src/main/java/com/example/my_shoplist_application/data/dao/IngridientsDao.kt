package com.example.my_shoplist_application.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.my_shoplist_application.data.entity.IngridientEntity

@Dao
interface IngridientsDao {

    @Upsert
    fun insertIngridient(ingridient: IngridientEntity)

    @Query("SELECT indridientId, ingridient_name, ingridient_quantity, ingridient_unit, ingridient_is_bought FROM ingridient_table")
    suspend fun getIngridients(): List<IngridientEntity>

}