package com.codebook.instatest.core.utils

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream


fun String.validateWithRegex(regex: String, message: String): String? {

    return if (this.isEmpty()) {
        null
    } else if (Regex(regex).matches(this).not()) {
        message
    } else {
        null
    }

}

fun String.isValidateWithRegex(regex: String): Boolean {

    return Regex(regex).matches(this)

}

fun String.validateJSON(message: String): String? {
    return try {
        JSONObject(this)
        null
    } catch (e: JSONException) {
        message
    }

}

fun String.isValidJson(): Boolean {
    return try {
        JSONObject(this)
        this.isEmpty().not()
    } catch (e: JSONException) {
        false
    }
}

fun String.splitTextIntoChunks(chunkSize: Int): List<String> {
    val chunks = mutableListOf<String>()
    var start = 0
    while (start < this.length) {
        val end = (start + chunkSize).coerceAtMost(this.length)
        chunks.add(this.substring(start, end))
        start = end
    }
    return chunks
}

fun ContentResolver.uriToByteArray(uri: Uri): ByteArray? {
    return try {
        val inputStream: InputStream? = this.openInputStream(uri)
        val byteBuffer = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len: Int

        if (inputStream != null) {
            while (inputStream.read(buffer).also { len = it } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
            inputStream.close()
        }

        byteBuffer.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun ContentResolver.getMimeType(uri: Uri): String? {
    // First, try to get the MIME type from the content resolver
    var mimeType: String? = this.getType(uri)

    // If MIME type is null, try to get it from the file extension
    if (mimeType == null) {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
    }

    return mimeType
}