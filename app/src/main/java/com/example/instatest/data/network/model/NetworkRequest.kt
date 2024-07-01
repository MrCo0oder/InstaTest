package com.example.instatest.data.network.model

class NetworkRequest private constructor(
    val url: String,
    val method: String,
    val headers: Map<String, String>,
    val body: String?,
    val parts: List<Part>?
) {
    class Builder {
        private var url: String = ""
        private var method: String = "GET"
        private var headers: MutableMap<String, String> = mutableMapOf()
        private var body: String? = null
        private var parts: MutableList<Part> = mutableListOf()

        fun url(url: String) = apply { this.url = url.trim() }
        fun method(method: String) = apply { this.method = method }
        fun addHeader(key: String, value: String) = apply { this.headers[key.trim()] = value.trim() }
        fun body(body: String?) = apply { this.body = body?.trim() }
        fun addPart(part: Part) = apply { this.parts.add(part) }

        fun build(): NetworkRequest {
            return NetworkRequest(url, method, headers, body, parts.takeIf { it.isNotEmpty() })
        }
    }

}
data class Part(
    val name: String,
    val fileName: String,
    val mimeType: String,
    val content: ByteArray
){
    override fun toString(): String {
        return "Part(name='$name', fileName='$fileName', mimeType='$mimeType', content=${content.contentToString()})"
    }
}