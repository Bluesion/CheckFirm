package com.illusion.checkfirm.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchRoute(
    onNavigationIconClick: () -> Unit,
    onSherlockClick: () -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyList by historyViewModel.historyList.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState,
        historyList = historyList,
        bookmarks = bookmarks,
        onModelChange = viewModel::updateModel,
        onCscChange = viewModel::updateCsc,
        onTabIndexChange = viewModel::updateSelectedTabIndex,
        onAddClick = viewModel::onAddClick,
        onDeviceClick = { viewModel.addToSearchList(it) },
        onRemoveFromSearchList = viewModel::removeFromSearchList,
        onDeleteHistory = { historyViewModel.delete(it.device.model, it.device.csc) },
        onDeleteAllHistory = historyViewModel::deleteAll,
        onSearchClick = {
            if (uiState.searchList.isNotEmpty()) {
                historyViewModel.createHistory(uiState.searchList)
                onNavigationIconClick()
            }
        },
        onNavigationIconClick = onNavigationIconClick
    )
}