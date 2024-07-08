package com.codebook.instatest.data

import com.codebook.instatest.core.enums.Methods
import com.codebook.instatest.data.network.model.Call
import com.codebook.instatest.data.network.model.Overview
import com.codebook.instatest.data.network.model.Request
import com.codebook.instatest.data.network.model.Response
import com.codebook.instatest.data.network.repo.HomeRepository
import java.io.File
import java.net.URL

class FakeFailurePostJSONRequestRepository : HomeRepository {
    override fun getRequest(url: String, headers: Map<String, String>): Call {
        throw NotImplementedError()
    }

    override fun postJsonRequest(
        url: String,
        headers: Map<String, String>,
        jsonBody: String?
    ): Call {
        Thread.sleep(1000) // Simulate network delay
        return Call(
            overview = Overview(url, Methods.POST.name, "Not Found", 404),
            request = Request(headers),
            response = Response(body = """{"success":"false","msg":"not found"}""")
        )
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
