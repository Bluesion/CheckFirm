package com.illusion.checkfirm.feature.home

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illusion.checkfirm.domain.model.SearchResult
import com.illusion.checkfirm.feature.home.component.NotificationPermissionDialog

@Composable
fun HomeRoute(
    onSearchIconClick: () -> Unit,
    onBookmarkIconClick: () -> Unit,
    onPreferenceIconClick: () -> Unit,
    onWelcomeSearchClick: () -> Unit,
    onInfoCatcherClick: () -> Unit,
    onOpenSherlock: (SearchResult) -> Unit,
    onOpenReport: (SearchResult) -> Unit,
    onOpenFirmwareManual: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    NotificationPermissionEffect()

    val visibleBookmarks = uiState.bookmarks.let { all ->
        if (uiState.selectedCategory.isBlank()) all
        else all.filter { it.category == uiState.selectedCategory }
    }

    HomeScreen(
        uiState = uiState,
        visibleBookmarks = visibleBookmarks,
        onSearchIconClick = onSearchIconClick,
        onBookmarkIconClick = onBookmarkIconClick,
        onPreferenceIconClick = onPreferenceIconClick,
        onWelcomeSearchClick = onWelcomeSearchClick,
        onInfoCatcherClick = onInfoCatcherClick,
        onCategoryIconClick = { viewModel.showCategoryDialog(true) },
        onCategoryPick = viewModel::updateSelectedCategory,
        onBookmarkChipClick = { bookmark ->
            viewModel.searchSingle(bookmark.device)
        },
        onCategoryDialogDismiss = { viewModel.showCategoryDialog(false) },
        onResultClick = viewModel::openResultDialog,
        onResultDismiss = viewModel::closeResultDialog,
        onCopy = { text -> copyToClipboard(context, text) },
        onOpenOfficialDoc = { result ->
            val url =
                "https://doc.samsungmobile.com/${result.device.model}/${result.device.csc}/doc.html"
            context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        },
        onOpenSherlock = { result ->
            viewModel.closeResultDialog()
            onOpenSherlock(result)
        },
        onOpenReport = { result ->
            viewModel.closeResultDialog()
            onOpenReport(result)
        },
        onOpenFirmwareManual = {
            viewModel.closeResultDialog()
            onOpenFirmwareManual()
        },
    )
}

private fun copyToClipboard(context: Context, text: String) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText("CheckFirm", text))
}

@Composable
private fun NotificationPermissionEffect() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val context = LocalContext.current
    val activity = remember(context) { context as? Activity }
    var showRationale by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (!granted &&
            activity?.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) == true
        ) {
            showRationale = true
        }
    }

    LaunchedEffect(Unit) {
        val alreadyGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
        if (!alreadyGranted) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    if (showRationale) {
        NotificationPermissionDialog(
            onGrant = {
                showRationale = false
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            onDismiss = { showRationale = false },
        )
    }
}
