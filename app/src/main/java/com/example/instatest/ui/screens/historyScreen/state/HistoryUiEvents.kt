package com.example.instatest.ui.screens.historyScreen.state

import com.example.instatest.data.database.model.NetworkCacheEntry

sealed class HistoryUiEvents {
    data class SortBy(val key: String) : HistoryUiEvents()
    data class FilterByMethod(val key: String) : HistoryUiEvents()
    data class FilterByStatus(val key: String) : HistoryUiEvents()
    data class AddList(val key: List<NetworkCacheEntry>) : HistoryUiEvents()
}