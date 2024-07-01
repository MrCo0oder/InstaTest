package com.example.instatest.data.database.repo


import com.example.instatest.data.database.dataSource.DatabaseHelper
import com.example.instatest.data.database.model.NetworkCacheEntry

class CacheRepositoryImpl(private val databaseHelper: DatabaseHelper) : CacheRepository {
    override fun getEntries(): MutableList<NetworkCacheEntry> {
        val cursor = databaseHelper.getAllEntries()
        val entries = mutableListOf<NetworkCacheEntry>()
        if (cursor.moveToFirst()) {
            do {
                val entry = NetworkCacheEntry(
                    url = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_URL)),
                    query = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUERY)),
                    method = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_METHOD)),
                    headers = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HEADERS)),
                    requestBody = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REQUEST_BODY)),
                    responseCode = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESPONSE_CODE)),
                    responseBody = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESPONSE_BODY)),
                    responseHeaders = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESPONSE_HEADERS)),
                    timestamp = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIMESTAMP))
                )
                entries.add(entry)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return entries
    }
}

