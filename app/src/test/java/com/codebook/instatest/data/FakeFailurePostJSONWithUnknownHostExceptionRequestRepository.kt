package com.codebook.instatest.data

import com.codebook.instatest.core.enums.Methods
import com.codebook.instatest.data.network.model.Call
import com.codebook.instatest.data.network.model.Overview
import com.codebook.instatest.data.network.model.Request
import com.codebook.instatest.data.network.model.Response
import com.codebook.instatest.data.network.repo.HomeRepository
import java.io.File
import java.net.URL
import java.net.UnknownHostException

class FakeFailurePostJSONWithUnknownHostExceptionRequestRepository : HomeRepository {
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
            overview = Overview(URL(url), Methods.POST.name, "Could not Resolve Host", -1),
            request = Request(
                headers = headers,
                body = jsonBody,
            ),
            response = Response(body = "Could not Resolve Host")
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
