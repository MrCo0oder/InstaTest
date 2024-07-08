package com.codebook.instatest.ui.screens.historyScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codebook.instatest.domain.local.GetAllEntriesUseCase

class CacheViewModelFactory(
    private var getAllEntriesUseCase: GetAllEntriesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CacheViewModel::class.java)) {
            return CacheViewModel(getAllEntriesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}