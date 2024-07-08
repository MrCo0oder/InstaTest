package com.codebook.instatest.data.network.repo

import com.codebook.instatest.core.enums.Methods
import com.codebook.instatest.data.database.dataSource.DatabaseHelper
import com.codebook.instatest.data.network.client.NetworkClientImpl
import com.codebook.instatest.data.network.model.Call
import com.codebook.instatest.data.network.model.NetworkRequest
import com.codebook.instatest.data.network.model.Part

import java.io.File
import java.net.HttpRetryException

class HomeRepositoryImpl(
    private val networkClientImpl: NetworkClientImpl,
    private val databaseHelper: DatabaseHelper
) : HomeRepository {
    override fun getRequest(url: String, headers: Map<String, String>): Call {
        val request = NetworkRequest.Builder()
            .url(url)
            .method(Methods.GET.name)
            .apply { headers.forEach { addHeader(it.key, it.value) } }
            .build()
        val networkCall = networkClientImpl.execute(request)
        databaseHelper.insertCacheEntry(networkCall)
        return networkCall
    }

    override fun postJsonRequest(
        url: String,
        headers: Map<String, String>,
        jsonBody: String?
    ): Call {

        val request = NetworkRequest.Builder()
            .url(url)
            .method(Methods.POST.name)
            .body(jsonBody)
            .apply { headers.forEach { addHeader(it.key, it.value) } }
            .build()
        val networkCall = networkClientImpl.execute(request)
        databaseHelper.insertCacheEntry(networkCall)
        return networkCall

    }

    override fun postMultipartRequest(
        url: String,
        headers: Map<String, String>,
        formKey: String,
        file: File,
        mimeType: String,
        fileContent: ByteArray
    ): Call {
        val part = Part(
            name = formKey,
            fileName = file.name,
            mimeType = mimeType,
            content = fileContent
        )
        val request = NetworkRequest.Builder()
            .url(url)
            .method(Methods.POST.name)
            .apply { headers.forEach { addHeader(it.key, it.value) } }
            .addPart(part)
            .build()
        val networkCall = networkClientImpl.execute(request)
        databaseHelper.insertCacheEntry(networkCall)
        return networkCall
    }
}

interface HomeRepository {
    fun getRequest(url: String, headers: Map<String, String>): Call

    fun postJsonRequest(url: String, headers: Map<String, String>, jsonBody: String?): Call

    fun postMultipartRequest(
        url: String,
        headers: Map<String, String>,
        formKey: String,
        file: File,
        mimeType: String,
        fileContent: ByteArray
    ): Call
}