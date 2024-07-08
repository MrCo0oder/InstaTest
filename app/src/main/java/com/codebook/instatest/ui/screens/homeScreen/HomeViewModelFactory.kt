package com.codebook.instatest.ui.screens.homeScreen

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codebook.instatest.domain.remote.GetRequestUseCase
import com.codebook.instatest.domain.remote.PostJsonRequestUseCase
import com.codebook.instatest.domain.remote.PostMultipartRequestUseCase

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