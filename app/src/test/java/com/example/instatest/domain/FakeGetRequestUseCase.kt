package com.example.instatest.domain

import com.example.instatest.data.network.model.Call
import com.example.instatest.data.network.repo.HomeRepository
import com.example.instatest.domain.remote.GetRequestUseCase
import com.example.instatest.ui.screens.homeScreen.states.NetworkState

class FakeGetRequestUseCase(private val fakeHomeRepository: HomeRepository) :
    GetRequestUseCase {
    override operator fun invoke(url: String, headers: Map<String, String>): NetworkState<Call> {
        return NetworkState.Success(fakeHomeRepository.getRequest(url, headers))
    }
}