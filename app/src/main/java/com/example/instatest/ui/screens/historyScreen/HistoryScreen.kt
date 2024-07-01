package com.example.instatest.ui.screens.historyScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.instatest.R
import com.example.instatest.core.enums.Methods
import com.example.instatest.core.utils.Constants
import com.example.instatest.core.utils.Constants.allKey
import com.example.instatest.core.utils.Constants.methodKey
import com.example.instatest.core.utils.Constants.statusKey
import com.example.instatest.core.utils.Constants.timestampKey
import com.example.instatest.ui.BodyText3Text
import com.example.instatest.ui.ButtonTextTwo
import com.example.instatest.ui.CachedCallItem
import com.example.instatest.ui.DropdownMenu
import com.example.instatest.ui.HeadLine2Text
import com.example.instatest.ui.screens.historyScreen.state.HistoryUiEvents
import com.example.instatest.ui.theme.PurplePlum


@Composable
fun HistoryScreen(navController: NavHostController, cacheViewModel: CacheViewModel = viewModel()) {
    val uiState = cacheViewModel.uiState.value
    LaunchedEffect(key1 = true) {
        if (uiState.sortedBy == timestampKey && uiState.filterByMethod == allKey && uiState.filterByStatus == allKey) {
            cacheViewModel.onEvent(HistoryUiEvents.AddList(cacheViewModel.networkCacheEntries()))
        }

    }

    Column {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.navigateUp() },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.history), tint = PurplePlum
                )
            }
            HeadLine2Text(stringResource(R.string.calls_history))
        }
        LazyRow(modifier = Modifier.padding(vertical = 20.dp)) {
            items(count = 1) {
                ButtonTextTwo(text = stringResource(R.string.sort_by))
                Spacer(modifier = Modifier.width(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                DropdownMenu(
                    uiState.sortedBy,
                    onSelectedChange = { cacheViewModel.onEvent(HistoryUiEvents.SortBy(it)) })
                Spacer(modifier = Modifier.width(16.dp))
                ButtonTextTwo(text = stringResource(R.string.filter_by_method))
                Spacer(modifier = Modifier.width(8.dp))
                DropdownMenu(
                    uiState.filterByMethod,
                    onSelectedChange = { cacheViewModel.onEvent(HistoryUiEvents.FilterByMethod(it)) },
                    items = listOf(
                        stringResource(id = R.string.all),
                        Methods.GET.name,
                        Methods.POST.name
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                ButtonTextTwo(text = stringResource(R.string.filter_by_status))
                Spacer(modifier = Modifier.width(8.dp))
                DropdownMenu(
                    uiState.filterByStatus,
                    onSelectedChange = {
                        cacheViewModel.onEvent(
                            HistoryUiEvents.FilterByStatus(it)
                        )
                    },
                    items = listOf(
                        stringResource(R.string.all),
                        stringResource(R.string.success), stringResource(R.string.failure)
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))

            }

        }

        // List of Cached Calls
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (uiState.list.isNotEmpty())
                items(uiState.list) { call ->
                    CachedCallItem(call)
                } else {
                item {
                    BodyText3Text(
                        text = stringResource(R.string.no_result_found),
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

