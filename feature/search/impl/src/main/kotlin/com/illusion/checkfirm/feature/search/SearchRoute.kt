package com.illusion.checkfirm.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState,
        historyList = historyList,
        bookmarks = bookmarks,
        onModelChange = viewModel::updateModel,
        onCscChange = viewModel::updateCsc,
        onAddClick = viewModel::onAddClick,
        onDeviceClick = viewModel::addToSearchList,
        onRemoveFromSearchList = viewModel::removeFromSearchList,
        onDeleteHistory = { viewModel.delete(it.device.model, it.device.csc) },
        onDeleteAllHistory = viewModel::deleteAll,
        onSearchClick = {
            if (uiState.searchList.isNotEmpty()) {
                viewModel.confirmAndEmit()
                onNavigationIconClick()
            }
        },
        onNavigationIconClick = onNavigationIconClick
    )
}
