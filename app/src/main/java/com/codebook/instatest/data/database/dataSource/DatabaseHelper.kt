package com.codebook.instatest.data.database.dataSource

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import com.codebook.instatest.data.network.model.Call
import java.net.URL

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

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

    /*url: String,query: String?, method: String, headers: String?, requestBody: String?, responseCode: Int, responseBody: String?,responseHeaders: String?,*/
    fun insertCacheEntry(call: Call): Long {
        try {
            val db = this.writableDatabase
            val contentValues = ContentValues().apply {
                put(COLUMN_URL, call.overview?.url.toString())
                put(COLUMN_QUERY, URL(call.overview?.url).query)
                put(COLUMN_METHOD, call.overview?.method)
                put(COLUMN_HEADERS, call.request?.headers.toString())
                put(COLUMN_REQUEST_BODY, call.request?.body)
                put(COLUMN_RESPONSE_CODE, call.overview?.statusCode)
                put(
                    COLUMN_RESPONSE_HEADERS,
                    if (call.response?.headerFields?.values?.isNotEmpty()==true) call.response?.headerFields.toString() else null
                )
                put(COLUMN_RESPONSE_BODY, call.response?.body)
            }

            val result = db.insert(TABLE_NAME, null, contentValues)
            return result
        } catch (e: Exception) {
            return -1
        }
    }

    fun getAllEntries(): Cursor {
        val db = this.readableDatabase
        val result = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_TIMESTAMP DESC"
        )
        return result
    }

}
