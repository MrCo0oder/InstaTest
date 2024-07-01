package com.example.instatest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instatest.domain.local.GetAllEntriesUseCase
import com.example.instatest.ui.screens.historyScreen.CacheViewModel

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