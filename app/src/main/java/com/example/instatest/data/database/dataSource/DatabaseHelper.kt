package com.example.instatest.data.database.dataSource

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "network_cache.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "NetworkCache"

        private const val COLUMN_ID = "id"
        const val COLUMN_URL = "url"
        const val COLUMN_QUERY = "query"
         const val COLUMN_METHOD = "method"
         const val COLUMN_HEADERS = "headers"
         const val COLUMN_REQUEST_BODY = "requestBody"
         const val COLUMN_RESPONSE_CODE = "responseCode"
         const val COLUMN_RESPONSE_BODY = "responseBody"
         const val COLUMN_RESPONSE_HEADERS = "responseHeaders"
         const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatement = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_URL TEXT NOT NULL,
                $COLUMN_QUERY TEXT,
                $COLUMN_METHOD TEXT NOT NULL,
                $COLUMN_HEADERS TEXT,
                $COLUMN_REQUEST_BODY TEXT,
                $COLUMN_RESPONSE_HEADERS TEXT,
                $COLUMN_RESPONSE_CODE INTEGER,
                $COLUMN_RESPONSE_BODY TEXT,
                $COLUMN_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()
        db?.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertCacheEntry(url: String,query: String?, method: String, headers: String?, requestBody: String?, responseCode: Int, responseBody: String?,responseHeaders: String?,): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_URL, url)
            put(COLUMN_QUERY, query)
            put(COLUMN_METHOD, method)
            put(COLUMN_HEADERS, headers)
            put(COLUMN_REQUEST_BODY, requestBody)
            put(COLUMN_RESPONSE_CODE, responseCode)
            put(COLUMN_RESPONSE_HEADERS, responseHeaders)
            put(COLUMN_RESPONSE_BODY, responseBody)
        }
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun getAllEntries(): Cursor {
        val db = this.readableDatabase
        return db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_TIMESTAMP DESC"
        )
    }

}
