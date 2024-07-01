package com.example.instatest.domain.remote

import com.example.instatest.data.network.model.Call
import com.example.instatest.data.network.repo.HomeRepository
import com.example.instatest.data.network.repo.HomeRepositoryImpl
import com.example.instatest.ui.screens.homeScreen.states.NetworkState

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
