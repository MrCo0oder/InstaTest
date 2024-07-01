package com.example.instatest.ui.screens.historyScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.instatest.core.utils.Constants.allKey
import com.example.instatest.core.utils.Constants.timestampKey
import com.example.instatest.domain.local.GetAllEntriesUseCase
import com.example.instatest.ui.screens.historyScreen.state.HistoryScreenState
import com.example.instatest.ui.screens.historyScreen.state.HistoryUiEvents

class CacheViewModel(private val getAllEntriesUseCase: GetAllEntriesUseCase) : ViewModel() {

    val uiState = mutableStateOf(HistoryScreenState())

init {
    networkCacheEntries()
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
            is HistoryUiEvents.AddList->{
                uiState.value=uiState.value.copy(list = event.key)
            }
        }

    }

}