package com.example.instatest.ui.screens.homeScreen

import android.content.ContentResolver
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instatest.core.enums.Methods
import com.example.instatest.core.enums.RequestBodyType
import com.example.instatest.core.utils.Constants.minTwoCharsRegex
import com.example.instatest.core.utils.Constants.urlRegex
import com.example.instatest.core.utils.getMimeType
import com.example.instatest.core.utils.isValidJson
import com.example.instatest.core.utils.isValidateWithRegex
import com.example.instatest.core.utils.uriToByteArray
import com.example.instatest.core.utils.validateWithRegex
import com.example.instatest.data.network.model.Call
import com.example.instatest.domain.remote.GetRequestUseCase
import com.example.instatest.domain.remote.PostJsonRequestUseCase
import com.example.instatest.domain.remote.PostMultipartRequestUseCase
import com.example.instatest.ui.screens.homeScreen.states.HomeScreenState
import com.example.instatest.ui.screens.homeScreen.states.HomeUiEvents
import com.example.instatest.ui.screens.homeScreen.states.NetworkState
import com.example.instatest.ui.screens.homeScreen.states.RequestHeader
import java.io.File
import java.util.concurrent.Executors

class HomeViewModel(
    private val getRequestUseCaseImpl: GetRequestUseCase,
    private val postJsonRequestUseCaseImpl: PostJsonRequestUseCase,
    private val postMultipartRequestUseCaseImpl: PostMultipartRequestUseCase,
    private val contentResolver: ContentResolver
) : ViewModel() {
    var uiState = mutableStateOf(HomeScreenState())
    val methodsList = Methods.entries
    val requestBodyTypeList = RequestBodyType.entries
    private val backgroundExecutor = Executors.newSingleThreadExecutor()

    private var _apiResponse: MutableLiveData<NetworkState<Call>> =
        MutableLiveData(NetworkState.Loading())
    val apiResponse: LiveData<NetworkState<Call>> = _apiResponse

    fun onEvent(event: HomeUiEvents) {
        uiState.value = reduce(uiState.value, event)
    }

    private fun reduce(state: HomeScreenState, event: HomeUiEvents): HomeScreenState {
        return when (event) {
            is HomeUiEvents.UrlEntered -> state.copy(url = event.url)

            is HomeUiEvents.MethodSelected -> state.copy(
                requestType = event.method,
                requestBodyType = if (event.method == Methods.GET) null else state.requestBodyType,
                fileUri = if (event.method == Methods.GET) null else state.fileUri,
            )

            is HomeUiEvents.RequestBodyTypeSelected -> state.copy(
                requestBodyType = event.requestBodyType,
                fileUri = if (event.requestBodyType == RequestBodyType.JSON) null else state.fileUri,
            )

            is HomeUiEvents.AddJSONBody -> state.copy(jsonBody = event.json)


            is HomeUiEvents.AddHeaderKey -> state.copy(headerKey = event.key)

            is HomeUiEvents.AddHeaderValue -> state.copy(headerValue = event.value)
            is HomeUiEvents.AddRequestHeader -> state.copy(
                headersList = (state.headersList + RequestHeader(
                    state.headerKey ?: "",
                    state.headerValue
                )).toMutableList(),
                headerKey = null,
                headerValue = "",
            )

            is HomeUiEvents.RemoveRequestHeader -> state.copy(
                headersList = state.headersList.toMutableList().apply {
                    removeAt(event.index)
                }
            )

            is HomeUiEvents.SelectFile -> state.copy(fileUri = event.fileUri)

            is HomeUiEvents.AddFromDataKey -> state.copy(formKey = event.key)
        }
    }

    fun isValidScreen(): Boolean =
        uiState.value.url.isValidateWithRegex(urlRegex) && uiState.value.requestType != null && isFormDataValidIfExist() && isRequestBodyTypeValidIfExist()

    private fun isFormDataValidIfExist(): Boolean = when (uiState.value.requestBodyType) {

        RequestBodyType.MULTIPART -> (Regex(minTwoCharsRegex).matches(
            uiState.value.formKey ?: ""
        ) && uiState.value.fileUri != null)

        RequestBodyType.JSON -> uiState.value.jsonBody?.isValidJson() == true
        else -> true
    }


    fun hitTheApi() {
        _apiResponse.postValue(NetworkState.Loading())
        val state = uiState.value

        when (state.requestType) {
            Methods.GET -> {

                Thread {
                    val call = getRequestUseCaseImpl.invoke(
                        state.url,
                        state.headersList.associate { it.key to it.value })
                    _apiResponse.postValue(call)
                }.start()
            }

            Methods.POST -> {
                when (state.requestBodyType) {
                    RequestBodyType.JSON -> {
                        Thread {
                            val call = postJsonRequestUseCaseImpl.invoke(
                                state.url,
                                state.headersList.associate { it.key to it.value },
                                state.jsonBody
                            )
                            _apiResponse.postValue(call)
                        }.start()
                    }

                    RequestBodyType.MULTIPART -> {
                        Thread {
                            val file = state.fileUri?.path?.let { File(it) }
                            val fileContent =
                                state.fileUri?.let { contentResolver.uriToByteArray(it) }
                            val fileMimeType =
                                state.fileUri?.let { contentResolver.getMimeType(it) }
                            if (file != null && fileContent != null && fileMimeType != null) {
                                val call = postMultipartRequestUseCaseImpl.invoke(
                                    state.url,
                                    state.headersList.associate { it.key to it.value },
                                    state.formKey!!,
                                    file,
                                    fileMimeType,
                                    fileContent
                                )
                                _apiResponse.postValue(call)
                            }
                        }.start()
                    }

                    else -> {}
                }
            }

            else -> {}
        }
    }

    private fun isRequestBodyTypeValidIfExist(): Boolean =
        if (uiState.value.requestType == Methods.POST) {
            uiState.value.requestBodyType != null
        } else
            true

}

