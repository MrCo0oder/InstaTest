package com.codebook.instatest.domain.remote

import com.codebook.instatest.data.network.model.Call
import com.codebook.instatest.data.network.repo.HomeRepository
import com.codebook.instatest.ui.screens.homeScreen.states.NetworkState

interface  GetRequestUseCase{
    fun invoke(url: String, headers: Map<String, String>): NetworkState<Call>
}
class GetRequestUseCaseImpl(private val repository: HomeRepository) :GetRequestUseCase{
     override operator fun invoke(url: String, headers: Map<String, String>): NetworkState<Call> {
        return NetworkState.Success(repository.getRequest(url, headers))
    }
}