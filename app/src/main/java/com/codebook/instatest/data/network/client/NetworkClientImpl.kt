package com.codebook.instatest.data.network.client

import com.codebook.instatest.data.database.dataSource.DatabaseHelper
import com.codebook.instatest.data.network.model.Call
import com.codebook.instatest.data.network.model.NetworkRequest
import com.codebook.instatest.data.network.model.Overview
import com.codebook.instatest.data.network.model.Part
import com.codebook.instatest.data.network.model.Request
import com.codebook.instatest.data.network.model.Response
import java.io.*
import java.net.HttpRetryException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.UnknownHostException
import java.util.*

interface NetworkClient {
    fun execute(request: NetworkRequest): Call
}

class NetworkClientImpl : NetworkClient {

    private val boundary = UUID.randomUUID().toString()
    private val lineEnd = "\r\n"
    private val twoHyphens = "--"
    private val boundaryPrefix = twoHyphens + boundary + lineEnd

    override fun execute(request: NetworkRequest): Call {
        try {
            val url = URL(request.url.trim().replace(" ", "%20"))
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = request.method
            for ((key, value) in request.headers) {
                connection.setRequestProperty(key, value)
            }

            if (request.parts != null) {
                connection.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=$boundary"
                )
                connection.doOutput = true
                val outputStream = DataOutputStream(connection.outputStream)
                writeMultipartData(outputStream, request.parts)
                outputStream.flush()
                outputStream.close()
            } else if (request.method == "POST") {
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                val outputStream = DataOutputStream(connection.outputStream)
                outputStream.writeBytes(request.body ?: "")
                outputStream.flush()
                outputStream.close()
            }

            val responseCode = connection.responseCode
            val inputStream: InputStream = if (responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }

            val response = inputStream.bufferedReader().use {
                it.readText()
            }

            val overview = Overview(
                url = request.url,
                method = request.method,
                status = connection.responseMessage,
                responseCode
            )

            inputStream.close()
            connection.disconnect()
            return Call(
                overview = overview,
                request = Request(
                    headers = request.headers,
                    body = request.body ?: buildString {
                        append(request.parts?.first().toString())
                    },
                ),
                response = Response(connection.headerFields, response)
            )
        } catch (e: HttpRetryException) {
            return handleNetworkException(request, e)
        } catch (e: UnknownHostException) {
            return handleNetworkException(request, e)
        } catch (e: MalformedURLException) {
            return handleNetworkException(request, e)
        } catch (e: Exception) {
            return handleNetworkException(request, e)
        }
    }

    private fun handleNetworkException(
        request: NetworkRequest,
        e: Exception,
    ): Call {
        val overview = Overview(
            url = request.url,
            method = request.method,
            status = e.message.toString(),
            statusCode = if (e is HttpRetryException) e.responseCode() else if (e is UnknownHostException) -1 else 0
        )
        return Call(
            overview = overview,
            request = Request(
                headers = request.headers,
                body = request.body ?: buildString {
                    append(request.parts?.first()?.toString())
                },
            ),
            response = Response(body = if (e is UnknownHostException) "Could not Resolve Host" else e.message)
        )
    }


    private fun writeMultipartData(outputStream: DataOutputStream, parts: List<Part>) {
        for (part in parts) {
            outputStream.writeBytes(boundaryPrefix)
            outputStream.writeBytes("Content-Disposition: form-data; name=\"${part.name}\"; filename=\"${part.fileName}\"$lineEnd")
            outputStream.writeBytes("Content-Type: ${part.mimeType}$lineEnd")
            outputStream.writeBytes(lineEnd)
            outputStream.write(part.content)
            outputStream.writeBytes(lineEnd)
        }
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
    }
}