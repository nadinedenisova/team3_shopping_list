package com.example.my_shoplist_application.di

import androidx.room.Room
import com.example.my_shoplist_application.db.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db")
            .build()
    }

}