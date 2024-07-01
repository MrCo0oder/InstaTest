package com.example.instatest.domain.local

import com.example.instatest.core.utils.Constants.allKey
import com.example.instatest.core.utils.Constants.failureKey
import com.example.instatest.core.utils.Constants.methodKey
import com.example.instatest.core.utils.Constants.statusKey
import com.example.instatest.core.utils.Constants.successKey
import com.example.instatest.core.utils.Constants.timestampKey
import com.example.instatest.data.database.model.NetworkCacheEntry
import com.example.instatest.data.database.repo.CacheRepository

interface GetAllEntriesUseCase {
    fun invoke(
        sortBy: String,
        filterByMethod: String,
        filterByStatus: String
    ): List<NetworkCacheEntry>
}

class GetAllEntriesUseCaseImpl(private val cacheRepository: CacheRepository) :
    GetAllEntriesUseCase {
    override operator fun invoke(
        sortBy: String,
        filterByMethod: String,
        filterByStatus: String
    ): List<NetworkCacheEntry> {
        return cacheRepository.getEntries()
            .filter { call -> filterByMethod == allKey || call.method == filterByMethod }
            .filter { call ->
                filterByStatus == allKey
                        || (filterByStatus == successKey && call.responseCode in 200..299)
                        || (filterByStatus == failureKey && call.responseCode !in 200..299)
            }
            .sortedBy { call ->
                when (sortBy) {
                    timestampKey -> {
                        return@sortedBy call.timestamp
                    }

                    methodKey-> {
                        return@sortedBy call.method
                    }

                    statusKey -> {
                        return@sortedBy call.responseCode.toString()
                    }

                    else -> {
                        return@sortedBy call.timestamp
                    }
                }
            }
    }
}