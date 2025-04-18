package com.example.my_shoplist_application.data.convertors

import com.example.my_shoplist_application.data.entity.IngridientEntity
import com.example.my_shoplist_application.domain.models.Ingridients

class IngridientsDbConvertor {
    fun map(ingridients: Ingridients): IngridientEntity {
        return IngridientEntity(
            ingridients.id,
            ingridients.ingridientName,
            ingridients.ingridientQuantity,
            ingridients.ingridientUnit,
            ingridients.isBought
        )
    }

    fun map(ingridients: IngridientEntity): Ingridients {
        return Ingridients(
            ingridients.indridientId,
            ingridients.ingridientName,
            ingridients.ingridientQuantity,
            ingridients.ingridientUnit,
            ingridients.isBought
        )
    }
}