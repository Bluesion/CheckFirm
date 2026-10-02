package com.illusion.checkfirm.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchRoute(
    onNavigationIconClick: () -> Unit,
    onAddBookmarkClick: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState,
        historyList = historyList,
        bookmarks = bookmarks,
        categories = categories,
        onAddBookmarkClick = onAddBookmarkClick,
        onSearchThisDevice = {
            val csc = runCatching {
                Runtime.getRuntime().exec(arrayOf("/system/bin/getprop", "ro.csc.sales_code"))
                    .inputStream.bufferedReader().use { it.readLine().orEmpty().uppercase(java.util.Locale.US) }
            }.getOrDefault("")
            viewModel.addToSearchList(com.illusion.checkfirm.core.domain.model.Device(android.os.Build.MODEL, csc))
        },
        onModelChange = viewModel::updateModel,
        onCscChange = viewModel::updateCsc,
        onAddClick = viewModel::onAddClick,
        onDeviceClick = viewModel::addToSearchList,
        onRemoveFromSearchList = viewModel::removeFromSearchList,
        onDeleteHistory = { viewModel.delete(it.device.model, it.device.csc) },
        onDeleteAllHistory = viewModel::deleteAll,
        onSearchClick = {
            if (uiState.searchList.isNotEmpty()) {
                scope.launch {
                    viewModel.confirmAndEmit()
                    onNavigationIconClick()
                }
            }
        },
        onNavigationIconClick = onNavigationIconClick
    )
}
