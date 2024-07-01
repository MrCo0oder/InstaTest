package com.example.instatest.ui.screens.homeScreen.states

sealed class NetworkState<T> {
    class Loading<T> : NetworkState<T>()
    data class Success<T>(val data: T) : NetworkState<T>()
    data class Error<T>(val error: Any) : NetworkState<T>()
}