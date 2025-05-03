package com.example.my_shoplist_application.domain.db

interface MainScreenListError {
    data object DataBaseError: MainScreenListError
    data object  NotFoundError: MainScreenListError
    data object UnknownError: MainScreenListError
}