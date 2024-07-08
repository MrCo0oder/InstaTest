package com.codebook.instatest.data.database.repo

import com.codebook.instatest.data.database.model.NetworkCacheEntry

interface CacheRepository {
    fun getEntries(): MutableList<NetworkCacheEntry>
}