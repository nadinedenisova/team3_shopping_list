package com.example.my_shoplist_application.di

import com.example.my_shoplist_application.data.convertors.IngredientsDbConvertor
import com.example.my_shoplist_application.data.convertors.ShoplistDbConvertor
import com.example.my_shoplist_application.data.entity.MainScreenRepositoryImpl
import com.example.my_shoplist_application.data.entity.ShoplistScreenRepositoryImpl
import com.example.my_shoplist_application.domain.db.MainScreenRepository
import com.example.my_shoplist_application.domain.db.ShoplistScreenRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<MainScreenRepository> {
        MainScreenRepositoryImpl(get(), get())
    }

    single<ShoplistScreenRepository> {
        ShoplistScreenRepositoryImpl(get(), get(), get(), get())
    }

    factory { IngredientsDbConvertor() }

    factory { ShoplistDbConvertor() }

}