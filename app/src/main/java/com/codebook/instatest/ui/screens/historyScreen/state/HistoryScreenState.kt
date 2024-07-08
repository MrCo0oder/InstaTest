package com.codebook.instatest.ui.screens.historyScreen.state

import com.codebook.instatest.data.database.model.NetworkCacheEntry

data class HistoryScreenState(
    var sortedBy: String = "timestamp",
    var filterByMethod: String = "All",
    var filterByStatus: String = "All",
    val list: List<NetworkCacheEntry> = mutableListOf()
)