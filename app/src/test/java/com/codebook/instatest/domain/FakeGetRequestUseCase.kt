package com.codebook.instatest.domain

import com.codebook.instatest.data.network.model.Call
import com.codebook.instatest.data.network.repo.HomeRepository
import com.codebook.instatest.domain.remote.GetRequestUseCase
import com.codebook.instatest.ui.screens.homeScreen.states.NetworkState

class FakeGetRequestUseCase(private val fakeHomeRepository: HomeRepository) :
    GetRequestUseCase {
    override operator fun invoke(url: String, headers: Map<String, String>): NetworkState<Call> {
        return NetworkState.Success(fakeHomeRepository.getRequest(url, headers))
    }
}