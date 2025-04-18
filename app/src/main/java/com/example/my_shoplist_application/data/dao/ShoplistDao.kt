package com.example.my_shoplist_application.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_shoplist_application.data.entity.ShoplistEntity
import com.example.my_shoplist_application.domain.models.Ingridients

@Dao
interface ShoplistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoplist(shoplist: ShoplistEntity)

    @Query("UPDATE shoplist_table SET shoplistName = :shoplistName, ingridientsList = :ingridientsList WHERE shoplistId = :shoplistId")
    suspend fun updateShoplist(
        shoplistId: Int,
        shoplistName: String,
        ingridientsList: MutableList<Ingridients>
    )

    @Query("SELECT * FROM shoplist_table ORDER BY addedAt DESC")
    suspend fun getShoplists(): List<ShoplistEntity>

    @Query("DELETE FROM shoplist_table WHERE shoplistId = :shoplistId")
    fun deleteShoplist(playlistId: Int)

    @Query("SELECT * FROM shoplist_table WHERE shoplistId = :shoplistId")
    suspend fun getShoplistById(shoplistId: Int): ShoplistEntity

}