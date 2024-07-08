package com.codebook.instatest.domain

import com.codebook.instatest.data.network.model.Call
import com.codebook.instatest.data.network.repo.HomeRepository
import com.codebook.instatest.domain.remote.PostJsonRequestUseCase
import com.codebook.instatest.ui.screens.homeScreen.states.NetworkState

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