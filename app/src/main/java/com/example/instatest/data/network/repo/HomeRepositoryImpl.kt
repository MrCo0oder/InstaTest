package com.example.instatest.data.network.repo

import com.example.instatest.data.network.client.NetworkClientImpl
import com.example.instatest.data.network.model.Call
import com.example.instatest.data.network.model.NetworkRequest
import com.example.instatest.data.network.model.Part

import java.io.File
import java.net.HttpRetryException

class HomeRepositoryImpl(private val networkClientImpl: NetworkClientImpl):HomeRepository {
    override fun getRequest(url: String, headers: Map<String, String>): Call {
        val request = NetworkRequest.Builder()
            .url(url)
            .method("GET")
            .apply { headers.forEach { addHeader(it.key, it.value) } }
            .build()
        return networkClientImpl.execute(request)

    }

    override fun postJsonRequest(url: String, headers: Map<String, String>, jsonBody: String?): Call {

        val request = NetworkRequest.Builder()
            .url(url)
            .method("POST")
            .body(jsonBody)
            .apply { headers.forEach { addHeader(it.key, it.value) } }
            .build()
        return networkClientImpl.execute(request)

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
            .method("POST")
            .apply { headers.forEach { addHeader(it.key, it.value) } }
            .addPart(part)
            .build()
        return networkClientImpl.execute(request)

    }
}

interface HomeRepository {
    fun getRequest(url: String, headers: Map<String, String>): Call

    fun postJsonRequest(url: String, headers: Map<String, String>, jsonBody: String?): Call

    fun postMultipartRequest(url: String, headers: Map<String, String>, formKey: String, file: File, mimeType: String, fileContent: ByteArray): Call
}