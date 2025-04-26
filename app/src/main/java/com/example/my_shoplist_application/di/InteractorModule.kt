package com.example.my_shoplist_application.di

import com.example.my_shoplist_application.domain.api.MainScreenInteractor
import com.example.my_shoplist_application.domain.api.ShoplistScreenInteractor
import com.example.my_shoplist_application.domain.impl.MainScreenInteractorImpl
import com.example.my_shoplist_application.domain.impl.ShoplistScreenInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<MainScreenInteractor> {
        MainScreenInteractorImpl(get())
    }

    single<ShoplistScreenInteractor> {
        ShoplistScreenInteractorImpl(get())
    }
}