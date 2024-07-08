package com.codebook.instatest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codebook.instatest.MyApplication
import com.codebook.instatest.core.di.AppContainer
import com.codebook.instatest.ui.navigation.AppNavigationGraph
import com.codebook.instatest.ui.screens.historyScreen.CacheViewModel
import com.codebook.instatest.ui.screens.historyScreen.CacheViewModelFactory
import com.codebook.instatest.ui.screens.homeScreen.HomeViewModel
import com.codebook.instatest.ui.screens.homeScreen.HomeViewModelFactory
import com.codebook.instatest.ui.theme.InstaTestTheme

class MainActivity : ComponentActivity() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var cacheViewModel: CacheViewModel
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = (application as MyApplication).appContainer
        setContent {
            InstaTestTheme {
                homeViewModel = viewModel(
                    factory = HomeViewModelFactory(
                        appContainer.getRequestUseCaseImpl,
                        appContainer.postJsonRequestUseCaseImpl,
                        appContainer.postMultipartRequestUseCaseImpl,
                        contentResolver
                    )
                )
                cacheViewModel = viewModel(
                    factory = CacheViewModelFactory(appContainer.getAllEntriesUseCaseImpl)
                )
                AppNavigationGraph(homeViewModel, cacheViewModel)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InstaTestTheme {
        AppNavigationGraph(viewModel(), viewModel())
    }
}