package com.example.my_shoplist_application.presentation

import android.app.Application
import com.example.my_shoplist_application.di.dataModule
import com.example.my_shoplist_application.di.interactorModule
import com.example.my_shoplist_application.di.repositoryModule
import com.example.my_shoplist_application.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }

    companion object {
        const val KEY_THEME_MODE = "key_theme_mode"
    }
}

