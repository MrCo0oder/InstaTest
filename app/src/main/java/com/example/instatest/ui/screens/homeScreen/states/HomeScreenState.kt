package com.example.instatest.ui.screens.homeScreen.states

import android.net.Uri
import com.example.instatest.core.enums.Methods
import com.example.instatest.core.enums.RequestBodyType

data class HomeScreenState(
    var url: String = "",
    var requestType: Methods? = null,
    var requestBodyType: RequestBodyType? = null,
    var jsonBody:String? = null,
    var formKey: String? = null,
    var headerKey: String? = null,
    var headerValue: String = "",
    var headersList: MutableList<RequestHeader> = mutableListOf(),
    var fileUri: Uri?=null
)