package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illusion.checkfirm.core.designsystem.R

@Composable
fun CategoryEditRoute(
    initialCategoryName: String?,
    onNavigationIconClick: () -> Unit,
    viewModel: CategoryEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val reservedAllLabel = stringResource(R.string.category_all)

    LaunchedEffect(initialCategoryName) { viewModel.initialize(initialCategoryName) }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                CategoryEditEvent.Saved -> onNavigationIconClick()
            }
        }
    }

    CategoryEditScreen(
        uiState = uiState,
        onNameChange = viewModel::updateName,
        onToggleBookmark = viewModel::toggleBookmark,
        onSave = { viewModel.save(reservedAllLabel) },
        onNavigationIconClick = onNavigationIconClick,
    )
}
