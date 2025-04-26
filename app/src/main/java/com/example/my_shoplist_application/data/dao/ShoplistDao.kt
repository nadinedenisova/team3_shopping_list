package com.example.my_shoplist_application.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.my_shoplist_application.data.entity.ShoplistEntity

@Dao
interface ShoplistDao {

    @Upsert
    fun insertShoplist(shoplist: ShoplistEntity)

    @Query("UPDATE shoplist_table SET shoplist_name = :shoplistName, shoplist_ingredients_list = :ingredientsIdList WHERE shoplist_id = :shoplistId")
    suspend fun updateShoplist(
        shoplistId: Int,
        shoplistName: String,
        ingredientsIdList: String
    )

    @Query("UPDATE shoplist_table SET shoplist_name = :shoplistName WHERE shoplist_id = :shoplistId")
    suspend fun renameShoplist(
        shoplistId: Int,
        shoplistName: String
    )

    @Query("SELECT shoplist_id, shoplist_name, shoplist_added_at, shoplist_ingredients_list, shoplist_is_pinned FROM shoplist_table ORDER BY shoplist_added_at DESC")
    suspend fun getShoplists(): List<ShoplistEntity>

    @Query("DELETE FROM shoplist_table WHERE shoplist_id = :shoplistId")
    fun deleteShoplist(shoplistId: Int)

    @Query("SELECT shoplist_id, shoplist_name, shoplist_added_at, shoplist_ingredients_list, shoplist_is_pinned FROM shoplist_table WHERE shoplist_id = :shoplistId")
    suspend fun getShoplistById(shoplistId: Int): ShoplistEntity

    @Query("SELECT shoplist_id, shoplist_name, shoplist_added_at, shoplist_ingredients_list, shoplist_is_pinned FROM shoplist_table WHERE shoplist_name= :shoplistName")
    suspend fun getShoplistByName(shoplistName: String): List<ShoplistEntity>

    @Query("UPDATE shoplist_table SET shoplist_ingredients_list = :ingredientsIdList WHERE shoplist_id = :shoplistId")
    suspend fun insertIngredientInShoplist(
        shoplistId: Int,
        ingredientsIdList: String
    )

    @Query("SELECT shoplist_ingredients_list FROM shoplist_table WHERE shoplist_id = :shoplistId")
    suspend fun getShoplistIngredients(shoplistId: Int): String

}