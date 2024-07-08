package com.codebook.instatest.data.database.model

data class NetworkCacheEntry(
    val url: String,
    val query: String?,
    val method: String,
    val headers: String?,
    val requestBody: String?,
    val responseCode: Int,
    val responseBody: String?,
    val responseHeaders: String?,
    val timestamp: String
)