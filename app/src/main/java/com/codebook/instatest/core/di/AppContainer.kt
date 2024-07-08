package com.codebook.instatest.core.di

import android.content.Context
import com.codebook.instatest.data.database.dataSource.DatabaseHelper
import com.codebook.instatest.data.database.repo.CacheRepositoryImpl
import com.codebook.instatest.data.network.client.NetworkClientImpl
import com.codebook.instatest.data.network.repo.HomeRepositoryImpl
import com.codebook.instatest.domain.local.GetAllEntriesUseCaseImpl
import com.codebook.instatest.domain.remote.GetRequestUseCaseImpl
import com.codebook.instatest.domain.remote.PostJsonRequestUseCaseImpl
import com.codebook.instatest.domain.remote.PostMultipartRequestUseCaseImpl

class AppContainer(context: Context) {
    companion object {
        private var instance: AppContainer? = null

        fun getAppContainer(context: Context): AppContainer {
            if (instance == null) {
                instance = AppContainer(context.applicationContext)
            }
            return instance!!
        }
    }

    private val dbHelper = DatabaseHelper(context)
    private val networkClient = NetworkClientImpl()
    private val homeRepositoryImpl = HomeRepositoryImpl(networkClient, dbHelper)
    private val cacheRepository = CacheRepositoryImpl(dbHelper)
    val getRequestUseCaseImpl = GetRequestUseCaseImpl(homeRepositoryImpl)
    val postJsonRequestUseCaseImpl = PostJsonRequestUseCaseImpl(homeRepositoryImpl)
    val postMultipartRequestUseCaseImpl = PostMultipartRequestUseCaseImpl(homeRepositoryImpl)
    val getAllEntriesUseCaseImpl = GetAllEntriesUseCaseImpl(cacheRepository)
}