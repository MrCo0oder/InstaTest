package com.codebook.instatest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codebook.instatest.ui.navigation.Routes.HISTORY_SCREEN
import com.codebook.instatest.ui.navigation.Routes.HOME_SCREEN
import com.codebook.instatest.ui.screens.historyScreen.CacheViewModel
import com.codebook.instatest.ui.screens.historyScreen.HistoryScreen
import com.codebook.instatest.ui.screens.homeScreen.HomeScreen
import com.codebook.instatest.ui.screens.homeScreen.HomeViewModel


@Composable
fun AppNavigationGraph(homeViewModel: HomeViewModel, cacheViewModel: CacheViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = HOME_SCREEN) {
        composable(HISTORY_SCREEN) {
            HistoryScreen(navController, cacheViewModel)
        }
        composable(HOME_SCREEN) {
            HomeScreen(navController, homeViewModel)
        }
    }
}

