package com.example.my_shoplist_application.di

import com.example.my_shoplist_application.presentation.main_screen.MainScreenViewModel
import com.example.my_shoplist_application.presentation.shoplist_screen.ShoplistScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainScreenViewModel(mainScreenInteractor = get())
    }

    viewModel {
        ShoplistScreenViewModel(shoplistScreenInteractor = get(), mainScreenInteractor = get())
    }

}
