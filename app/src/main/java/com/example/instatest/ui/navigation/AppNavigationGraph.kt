package com.example.instatest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instatest.data.database.dataSource.DatabaseHelper
import com.example.instatest.data.database.repo.CacheRepositoryImpl
import com.example.instatest.data.network.client.NetworkClientImpl
import com.example.instatest.data.network.repo.HomeRepositoryImpl
import com.example.instatest.domain.local.GetAllEntriesUseCaseImpl
import com.example.instatest.domain.remote.GetRequestUseCaseImpl
import com.example.instatest.domain.remote.PostJsonRequestUseCaseImpl
import com.example.instatest.domain.remote.PostMultipartRequestUseCaseImpl
import com.example.instatest.ui.CacheViewModelFactory
import com.example.instatest.ui.HomeViewModelFactory
import com.example.instatest.ui.navigation.Routes.HISTORY_SCREEN
import com.example.instatest.ui.navigation.Routes.HOME_SCREEN
import com.example.instatest.ui.screens.historyScreen.CacheViewModel
import com.example.instatest.ui.screens.historyScreen.HistoryScreen
import com.example.instatest.ui.screens.homeScreen.HomeScreen
import com.example.instatest.ui.screens.homeScreen.HomeViewModel


@Composable
fun AppNavigationGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val dbHelper = DatabaseHelper(context)

    val homeRepositoryImpl = HomeRepositoryImpl(NetworkClientImpl(dbHelper))
    val cacheRepository = CacheRepositoryImpl(dbHelper)

    val getRequestUseCaseImpl = GetRequestUseCaseImpl(homeRepositoryImpl)
    val postJsonRequestUseCaseImpl = PostJsonRequestUseCaseImpl(homeRepositoryImpl)
    val postMultipartRequestUseCaseImpl = PostMultipartRequestUseCaseImpl(homeRepositoryImpl)
    val getAllEntriesUseCaseImpl = GetAllEntriesUseCaseImpl(cacheRepository)

    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            getRequestUseCaseImpl,
            postJsonRequestUseCaseImpl,
            postMultipartRequestUseCaseImpl,
            context.contentResolver
        )
    )
    val cacheViewModel: CacheViewModel = viewModel(
        factory = CacheViewModelFactory(getAllEntriesUseCaseImpl)
    )

    NavHost(navController = navController, startDestination = HOME_SCREEN) {
        composable(HISTORY_SCREEN) {
            HistoryScreen(navController, cacheViewModel)
        }
        composable(HOME_SCREEN) {
            HomeScreen(navController, homeViewModel)
        }
    }
}

