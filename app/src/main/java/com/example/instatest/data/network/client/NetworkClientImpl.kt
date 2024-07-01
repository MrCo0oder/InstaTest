package com.example.instatest.data.network.client

import com.example.instatest.data.database.dataSource.DatabaseHelper
import com.example.instatest.data.network.model.Call
import com.example.instatest.data.network.model.NetworkRequest
import com.example.instatest.data.network.model.Overview
import com.example.instatest.data.network.model.Part
import com.example.instatest.data.network.model.Request
import com.example.instatest.data.network.model.Response
import java.io.*
import java.net.HttpRetryException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.UnknownHostException
import java.util.*
interface NetworkClient{
    fun execute(request: NetworkRequest):Call
}
class NetworkClientImpl(private val dbHelper: DatabaseHelper): NetworkClient{

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
                url = URL(request.url),
                method = request.method,
                status = connection.responseMessage,
                responseCode
            )

            inputStream.close()
            connection.disconnect()
            dbHelper.insertCacheEntry(
                request.url, URL(request.url).query, request.method, request.headers.toString(),
                request.body,
                responseCode,
                response,
                responseHeaders = connection.headerFields.toString()
            )
            return Call(
                overview = overview,
                request = Request(
                    headers = request.headers,
                    body = request.body ?: buildString {
                        append(request.parts?.get(0).toString())
                        append("...")
                    },
                ),
                response = Response(connection.headerFields, response)
            )
        } catch (e: HttpRetryException) {
            return handleNetworkException(request, e,dbHelper)
        } catch (e: UnknownHostException) {
            return handleNetworkException(request, e,dbHelper)
        } catch (e: MalformedURLException) {
            return handleNetworkException(request, e,dbHelper)
        } catch (e: Exception) {
            return handleNetworkException(request, e,dbHelper)
        }
    }

    private fun handleNetworkException(request: NetworkRequest, e: Exception,dbHelper: DatabaseHelper): Call {
        val overview = Overview(
            url = URL(request.url),
            method = request.method,
            status = e.message.toString(),
            statusCode = 0
        )
        dbHelper.insertCacheEntry(
            request.url, URL(request.url).query, request.method, request.headers.toString(),
            request.body ?: buildString {
                append(request.parts.toString())
            },
            if (e is HttpRetryException) e.responseCode() else if (e is UnknownHostException) -1 else 0,
            e.message,
            null

        )
        return Call(
            overview = overview,
            request = Request(
                headers = request.headers,
                body = request.body ?: buildString {
                    append(request.parts?.toString())
                    append("...")
                },
            ),
            response = Response(body =  if (e is UnknownHostException) "Could not Resolve Host" else "")
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