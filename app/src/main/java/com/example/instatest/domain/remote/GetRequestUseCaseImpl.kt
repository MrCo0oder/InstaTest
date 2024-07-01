package com.example.instatest.domain.remote

import com.example.instatest.data.network.model.Call
import com.example.instatest.data.network.repo.HomeRepository
import com.example.instatest.data.network.repo.HomeRepositoryImpl
import com.example.instatest.ui.screens.homeScreen.states.NetworkState

interface  GetRequestUseCase{
    fun invoke(url: String, headers: Map<String, String>): NetworkState<Call>
}
class GetRequestUseCaseImpl(private val repository: HomeRepository) :GetRequestUseCase{
     override operator fun invoke(url: String, headers: Map<String, String>): NetworkState<Call> {
        return NetworkState.Success(repository.getRequest(url, headers))
    }
}