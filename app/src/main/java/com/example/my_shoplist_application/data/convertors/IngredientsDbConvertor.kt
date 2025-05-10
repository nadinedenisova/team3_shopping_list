package com.example.my_shoplist_application.data.convertors

import com.example.my_shoplist_application.data.entity.IngredientEntity
import com.example.my_shoplist_application.domain.models.Ingredients

class IngredientsDbConvertor {
    fun map(ingredients: Ingredients): IngredientEntity {
        return IngredientEntity(
            ingredients.id,
            ingredients.ingredientName,
            ingredients.ingredientQuantity,
            ingredients.ingredientUnit,
            ingredients.shopListId,
            ingredients.isBought
        )
    }

    fun map(ingredients: IngredientEntity): Ingredients {
        return Ingredients(
            ingredients.ingredientId,
            ingredients.ingredientName,
            ingredients.ingredientQuantity,
            ingredients.ingredientUnit,
            ingredients.shopListId,
            ingredients.isBought
        )
    }
}
