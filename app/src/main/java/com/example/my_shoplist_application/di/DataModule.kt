package com.example.my_shoplist_application.di

import android.content.Context.MODE_PRIVATE
import androidx.room.Room
import com.example.my_shoplist_application.data.shared.PRACTICUM_EXAMPLE_PREFERENCES
import com.example.my_shoplist_application.data.shared.SharedManager
import com.example.my_shoplist_application.db.AppDataBase
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    factory { Gson() }

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db")
            .build()
    }

    single { SharedManager(get()) }

    single {
        androidContext().getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
    }
}