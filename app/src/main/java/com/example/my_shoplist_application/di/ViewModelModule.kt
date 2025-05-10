package com.example.my_shoplist_application.di

import com.example.my_shoplist_application.presentation.MainScreenViewModel
import com.example.my_shoplist_application.presentation.ShoplistScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainScreenViewModel(get())
    }

    viewModel {
        ShoplistScreenViewModel(get(), get())
    }

}
