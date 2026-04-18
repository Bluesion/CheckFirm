package com.illusion.checkfirm.feature.settings.backuprestore

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class BackupRestoreUiState(val dummy: Boolean = false)

@HiltViewModel
class BackupRestoreViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(BackupRestoreUiState())
    val uiState: StateFlow<BackupRestoreUiState> = _uiState.asStateFlow()

    fun backup(uri: android.net.Uri) {
        // Implementation to write bookmarks to JSON goes here
    }

    fun restore(uri: android.net.Uri) {
        // Implementation to read bookmarks from JSON goes here
    }
}
