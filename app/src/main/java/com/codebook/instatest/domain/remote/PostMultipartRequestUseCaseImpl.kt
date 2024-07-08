package com.codebook.instatest.domain.remote

import com.codebook.instatest.data.network.model.Call
import com.codebook.instatest.data.network.repo.HomeRepository
import com.codebook.instatest.ui.screens.homeScreen.states.NetworkState
import java.io.File

class PostMultipartRequestUseCaseImpl(private val repository: HomeRepository) :
    PostMultipartRequestUseCase {
    override operator fun invoke(
        url: String,
        headers: Map<String, String>,
        formKey: String,
        file: File,
        mimeType: String,
        fileContent: ByteArray
    ): NetworkState.Success<Call> {
        return NetworkState.Success(
            repository.postMultipartRequest(
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
interface PostMultipartRequestUseCase {
    operator fun invoke(
        url: String,
        headers: Map<String, String>,
        formKey: String,
        file: File,
        mimeType: String,
        fileContent: ByteArray
    ): NetworkState.Success<Call>
}