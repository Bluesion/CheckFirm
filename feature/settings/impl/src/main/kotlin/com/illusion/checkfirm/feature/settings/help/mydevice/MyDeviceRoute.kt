package com.illusion.checkfirm.feature.settings.help.mydevice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.illusion.checkfirm.feature.bookmark.impl.BookmarkDialog
import com.illusion.checkfirm.feature.bookmark.impl.BookmarkViewModel
import com.illusion.checkfirm.core.designsystem.R
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MyDeviceRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: MyDeviceViewModel = hiltViewModel(),
    bookmarkViewModel: BookmarkViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current
    var showBookmark by rememberSaveable { mutableStateOf(false) }
    val bookmarkState by bookmarkViewModel.uiState.collectAsStateWithLifecycle()
    MyDeviceScreen(uiState = uiState, onNavigationIconClick = onNavigationIconClick,
        onEditDeviceName = { context.startActivity(android.content.Intent(android.provider.Settings.ACTION_DEVICE_INFO_SETTINGS)) },
        onAddBookmark = { showBookmark = true },
    )
    if (showBookmark) BookmarkDialog(
        categories = listOf(stringResource(R.string.category_all)) + bookmarkState.categories.map { it.name },
        onDismiss = { showBookmark = false },
        onConfirm = { bookmarkViewModel.addBookmark(it); showBookmark = false },
    )
}
