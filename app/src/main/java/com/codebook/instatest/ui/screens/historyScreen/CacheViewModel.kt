package com.codebook.instatest.ui.screens.historyScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.codebook.instatest.core.utils.Constants.allKey
import com.codebook.instatest.core.utils.Constants.timestampKey
import com.codebook.instatest.domain.local.GetAllEntriesUseCase
import com.codebook.instatest.ui.screens.historyScreen.state.HistoryScreenState
import com.codebook.instatest.ui.screens.historyScreen.state.HistoryUiEvents
import java.util.concurrent.Executors

class CacheViewModel(private val getAllEntriesUseCase: GetAllEntriesUseCase) : ViewModel() {

    val uiState = mutableStateOf(HistoryScreenState())
    private val backgroundExecutor = Executors.newSingleThreadExecutor()

    init {
        backgroundExecutor.execute {
            networkCacheEntries()
        }
    }

    fun networkCacheEntries() = getAllEntriesUseCase.invoke(timestampKey, allKey, allKey)

    fun onEvent(event: HistoryUiEvents) {
        when (event) {
            is HistoryUiEvents.SortBy -> {
                uiState.value = uiState.value.copy(
                    sortedBy = event.key,
                    list = getAllEntriesUseCase.invoke(
                        event.key,
                        uiState.value.filterByMethod,
                        uiState.value.filterByStatus
                    )
                )
            }

            is HistoryUiEvents.FilterByMethod -> {
                uiState.value = uiState.value.copy(
                    filterByMethod = event.key,
                    list = getAllEntriesUseCase.invoke(
                        uiState.value.sortedBy,
                        event.key,
                        uiState.value.filterByStatus
                    )
                )

            }

            is HistoryUiEvents.FilterByStatus -> {
                uiState.value = uiState.value.copy(
                    filterByStatus = event.key,
                    list = getAllEntriesUseCase.invoke(
                        uiState.value.sortedBy,
                        uiState.value.filterByMethod,
                        event.key
                    )
                )
            }

            is HistoryUiEvents.AddList -> {
                uiState.value = uiState.value.copy(list = event.key)
            }
        }

    }

    override fun onCleared() {
        if (backgroundExecutor.isShutdown.not())
            backgroundExecutor.shutdown()
        super.onCleared()
    }

}