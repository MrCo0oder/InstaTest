package com.example.instatest.ui

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instatest.domain.remote.GetRequestUseCase
import com.example.instatest.domain.remote.GetRequestUseCaseImpl
import com.example.instatest.domain.remote.PostJsonRequestUseCase
import com.example.instatest.domain.remote.PostJsonRequestUseCaseImpl
import com.example.instatest.domain.remote.PostMultipartRequestUseCase
import com.example.instatest.domain.remote.PostMultipartRequestUseCaseImpl
import com.example.instatest.ui.screens.homeScreen.HomeViewModel

class HomeViewModelFactory(
    private var getRequestUseCaseImpl: GetRequestUseCase,
    private var postJsonRequestUseCaseImpl: PostJsonRequestUseCase,
    private var postMultipartRequestUseCaseImpl: PostMultipartRequestUseCase, private var contentResolver: ContentResolver
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(getRequestUseCaseImpl,postJsonRequestUseCaseImpl,postMultipartRequestUseCaseImpl,contentResolver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}