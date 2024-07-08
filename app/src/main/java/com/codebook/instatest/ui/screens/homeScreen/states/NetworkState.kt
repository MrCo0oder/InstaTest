package com.codebook.instatest.ui.screens.homeScreen.states

sealed class NetworkState<T> {
    class Loading<T> : NetworkState<T>()
    data class Success<T>(val data: T) : NetworkState<T>()
}