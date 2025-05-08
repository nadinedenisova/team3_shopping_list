package com.example.my_shoplist_application.presentation.model

import androidx.compose.ui.geometry.Offset
import com.example.my_shoplist_application.domain.models.Ingredients
import com.example.my_shoplist_application.domain.models.MeasurementUnit

data class IngredientListState(
    val ingredients: List<Ingredients> = emptyList(),
    val sortOrder: SortOrder = SortOrder.CUSTOM,
    val newItemName: String = "",
    val newItemQuantity: Int = 0,
    val newItemUnit: MeasurementUnit = MeasurementUnit.PCS,
    val suggestions: List<String> = emptyList(),
    val showContextMenu: Boolean = false,
    val contextMenuPosition: Offset = Offset.Zero,
    val isAdding: Boolean = false,
    val ingredient: Ingredients = Ingredients(0, "", 0, MeasurementUnit.PCS),
  //  val showRenamePanel: Boolean = false,
    val editingIngredientId: Int = 0,
    val showAddPanel: Boolean = false,
    val isAllChecked: Boolean = false
) {

    enum class SortOrder {
        CUSTOM, ALPHABETICAL
    }
}