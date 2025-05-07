package com.example.my_shoplist_application.domain.models

import android.content.Context
import com.example.my_shoplist_application.R

enum class MeasurementUnit {
    KG, L, UP, PCS,
}

fun MeasurementUnit.getLabel(context: Context): String {
    return when (this) {
        MeasurementUnit.KG -> context.getString(R.string.measurement_kg)
        MeasurementUnit.L -> context.getString(R.string.measurement_l)
        MeasurementUnit.UP -> context.getString(R.string.measurement_ml)
        MeasurementUnit.PCS -> context.getString(R.string.measurement_pcs)

    }
}