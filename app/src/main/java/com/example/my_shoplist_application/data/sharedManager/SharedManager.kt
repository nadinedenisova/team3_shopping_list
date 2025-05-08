package com.example.my_shoplist_application.data.sharedManager

import android.content.SharedPreferences
import androidx.core.content.edit

const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"
const val SWITCH_STATUS = "SWITCH_STATUS"

class SharedManager(private val sharedPreferences: SharedPreferences) {

    fun putSwitchStatus(isChecked: Boolean) {
        sharedPreferences.edit { putBoolean(SWITCH_STATUS, isChecked) }
    }

    fun getSwitchStatus(): Boolean {
        return sharedPreferences.getBoolean(SWITCH_STATUS, false)
    }
}