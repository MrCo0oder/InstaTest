package com.codebook.instatest

import android.app.Application
import com.codebook.instatest.core.di.AppContainer


class MyApplication : Application() {

    // Instance of AppContainer that will be used by all the Activities of the app
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer.getAppContainer(applicationContext)
    }
}