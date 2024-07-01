package com.example.instatest.data

import com.example.instatest.core.enums.Methods
import com.example.instatest.data.network.model.Call
import com.example.instatest.data.network.model.Overview
import com.example.instatest.data.network.model.Request
import com.example.instatest.data.network.model.Response
import com.example.instatest.data.network.repo.HomeRepository
import java.io.File
import java.net.URL

class FakeFailureGetRequestHomeRepository : HomeRepository {
    override fun getRequest(url: String, headers: Map<String, String>): Call {
        Thread.sleep(1000) // Simulate network delay
        return  Call(
            overview = Overview(URL(url), Methods.GET.name, "Server Error", 500),
            request = Request(headers),
            response = Response(body = """{"success":"false","msg":"something went wrong!"}""")
        )
    }

    override fun postJsonRequest(
        url: String,
        headers: Map<String, String>,
        jsonBody: String?
    ): Call {
        throw NotImplementedError()
    }


    override fun postMultipartRequest(
        url: String,
        headers: Map<String, String>,
        formKey: String,
        file: File,
        mimeType: String,
        fileContent: ByteArray
    ): Call {
        throw NotImplementedError()
    }
}
