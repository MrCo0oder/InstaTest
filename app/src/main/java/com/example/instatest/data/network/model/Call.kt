package com.example.instatest.data.network.model

import java.net.URL

data class Call(
    var overview: Overview? = null,
    var request: Request? = null,
    var response: Response? = null
) {
    override fun toString(): String {
        return "Call(overview=${overview.toString()}, request=$request, response=$response)"
    }
}

data class Request(var headers: Map<String, String> = mutableMapOf(), var body: String? = null) {
    override fun toString(): String {
        return "Request(headers=$headers, body=$body)"
    }
}

data class Overview(val url: URL, val method: String, val status: String, val statusCode: Int) {
    override fun toString(): String {
        return "Overview(url='${url.query}', method='$method', status='$status', statusCode=$statusCode)"
    }
}

data class Response(
    var headerFields: Map<String, List<String>> = mutableMapOf(),
    var body: String? = null
) {
    override fun toString(): String {
        return "Response(headerFields=$headerFields, body=$body)"
    }
}