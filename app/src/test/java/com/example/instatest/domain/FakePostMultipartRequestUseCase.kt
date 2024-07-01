package com.example.instatest.domain

import com.example.instatest.data.network.model.Call
import com.example.instatest.data.network.repo.HomeRepository
import com.example.instatest.domain.remote.PostMultipartRequestUseCase
import com.example.instatest.ui.screens.homeScreen.states.NetworkState
import java.io.File

class FakePostMultipartRequestUseCase(private val fakeHomeRepository: HomeRepository) :
    PostMultipartRequestUseCase {
    override fun invoke(
        url: String,
        headers: Map<String, String>,
        formKey: String,
        file: File,
        mimeType: String,
        fileContent: ByteArray
    ): NetworkState.Success<Call> {
        return NetworkState.Success(
            fakeHomeRepository.postMultipartRequest(
                url,
                headers,
                formKey,
                file,
                mimeType,
                fileContent
            )
        )
    }
}