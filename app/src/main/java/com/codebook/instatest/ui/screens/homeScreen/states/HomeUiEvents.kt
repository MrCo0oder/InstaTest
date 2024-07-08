package com.codebook.instatest.ui.screens.homeScreen.states

import android.net.Uri
import com.codebook.instatest.core.enums.Methods
import com.codebook.instatest.core.enums.RequestBodyType

sealed class HomeUiEvents {
    data class UrlEntered(val url: String) : HomeUiEvents()
    data class MethodSelected(val method: Methods) : HomeUiEvents()
    data class RequestBodyTypeSelected(val requestBodyType: RequestBodyType) : HomeUiEvents()
    data class AddJSONBody(val json: String) : HomeUiEvents()
    data class AddRequestHeader(val requestHeaders: RequestHeader) : HomeUiEvents()
    data class RemoveRequestHeader(val index: Int) : HomeUiEvents()
    data class AddFromDataKey(val key: String) : HomeUiEvents()
    data class AddHeaderKey(val key: String) : HomeUiEvents()
    data class AddHeaderValue(val value: String) : HomeUiEvents()
    data class SelectFile(val fileUri: Uri?) : HomeUiEvents()
}