package com.example.instatest.data.database.repo

import com.example.instatest.data.database.model.NetworkCacheEntry

interface CacheRepository {
    fun getEntries(): MutableList<NetworkCacheEntry>
}