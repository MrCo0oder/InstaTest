package com.example.instatest.domain

import com.example.instatest.data.network.model.Call
import com.example.instatest.data.network.repo.HomeRepository
import com.example.instatest.domain.remote.PostJsonRequestUseCase
import com.example.instatest.ui.screens.homeScreen.states.NetworkState

class FakePostJsonRequestUseCase(private val fakeHomeRepository: HomeRepository) :
    PostJsonRequestUseCase {
    override fun invoke(
        url: String,
        headers: Map<String, String>,
        jsonBody: String?
    ): NetworkState<Call> {
        return NetworkState.Success(fakeHomeRepository.postJsonRequest(url, headers, jsonBody))
    }
}