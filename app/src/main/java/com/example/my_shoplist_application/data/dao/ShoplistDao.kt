package com.example.my_shoplist_application.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.my_shoplist_application.data.entity.ShoplistEntity
import com.example.my_shoplist_application.domain.models.Ingredients

@Dao
interface ShoplistDao {

    @Upsert
    fun insertShoplist(shoplist: ShoplistEntity)

    @Query("UPDATE shoplist_table SET shoplist_name = :shoplistName, shoplist_ingredients_list = :ingredientsList WHERE shoplistId = :shoplistId")
    suspend fun updateShoplist(
        shoplistId: Int,
        shoplistName: String,
        ingredientsList: MutableList<Ingredients>
    )

    @Query("UPDATE shoplist_table SET shoplist_name = :shoplistName WHERE shoplistId = :shoplistId")
    suspend fun renameShoplist(
        shoplistId: Int,
        shoplistName: String
    )

    @Query("SELECT shoplistId, shoplist_name, shoplist_added_at, shoplist_ingredients_list, shoplist_is_pinned FROM shoplist_table ORDER BY shoplist_added_at DESC")
    suspend fun getShoplists(): List<ShoplistEntity>

    @Query("DELETE FROM shoplist_table WHERE shoplistId = :shoplistId")
    fun deleteShoplist(shoplistId: Int)

    @Query("SELECT shoplistId, shoplist_name, shoplist_added_at, shoplist_ingredients_list, shoplist_is_pinned FROM shoplist_table WHERE shoplistId = :shoplistId")
    suspend fun getShoplistById(shoplistId: Int): ShoplistEntity

}