package com.example.my_shoplist_application.domain.db

sealed interface Result<Data, Error> {
    class Success<Data, Error>(val data: Data) : Result<Data, Error>
    class Error<Data, Error>(val error: Error) : Result<Data, Error>
}