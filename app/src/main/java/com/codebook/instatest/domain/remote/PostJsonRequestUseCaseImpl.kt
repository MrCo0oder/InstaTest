package com.codebook.instatest.domain.remote

import com.codebook.instatest.data.network.model.Call
import com.codebook.instatest.data.network.repo.HomeRepository
import com.codebook.instatest.data.network.repo.HomeRepositoryImpl
import com.codebook.instatest.ui.screens.homeScreen.states.NetworkState

class PostJsonRequestUseCaseImpl(private val repository: HomeRepository) : PostJsonRequestUseCase {
    override operator fun invoke(
        url: String,
        headers: Map<String, String>,
        jsonBody: String?
    ): NetworkState<Call> {
        return NetworkState.Success(repository.postJsonRequest(url, headers, jsonBody))
    }
}

interface PostJsonRequestUseCase {
    fun invoke(url: String, headers: Map<String, String>, jsonBody: String?): NetworkState<Call>
}
