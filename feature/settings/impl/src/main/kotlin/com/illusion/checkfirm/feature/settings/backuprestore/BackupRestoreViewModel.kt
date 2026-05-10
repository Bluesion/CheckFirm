package com.illusion.checkfirm.feature.settings.backuprestore

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.core.domain.repository.BCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

data class BackupRestoreUiState(val isWorking: Boolean = false)

sealed interface BackupRestoreEvent {
    data object BackupSuccess : BackupRestoreEvent
    data object BackupFail : BackupRestoreEvent
    data object RestoreSuccess : BackupRestoreEvent
    data object RestoreFail : BackupRestoreEvent
}

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    private val application: Application,
    private val bcRepository: BCRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BackupRestoreUiState())
    val uiState: StateFlow<BackupRestoreUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BackupRestoreEvent>()
    val events: SharedFlow<BackupRestoreEvent> = _events.asSharedFlow()

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun backup(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = BackupRestoreUiState(isWorking = true)
            val ok = try {
                val item = BackupItem(
                    bookmarkList = bcRepository.getAllBookmark("date", true).first()
                        .map(BookmarkDto::fromDomain),
                    categoryList = bcRepository.getAllCategory().first()
                        .map(CategoryDto::fromDomain),
                )
                val payload = json.encodeToString(BackupItem.serializer(), item)
                withContext(Dispatchers.IO) {
                    val stream = application.contentResolver.openOutputStream(uri)
                        ?: error("openOutputStream returned null for $uri")
                    stream.use { it.write(payload.toByteArray()) }
                }
                true
            } catch (t: Throwable) {
                false
            }
            _uiState.value = BackupRestoreUiState(isWorking = false)
            _events.emit(if (ok) BackupRestoreEvent.BackupSuccess else BackupRestoreEvent.BackupFail)
        }
    }

    fun restore(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = BackupRestoreUiState(isWorking = true)
            val ok = try {
                val text = withContext(Dispatchers.IO) {
                    val stream = application.contentResolver.openInputStream(uri)
                        ?: error("openInputStream returned null for $uri")
                    stream.use { it.bufferedReader().readText() }
                }
                val parsed = json.decodeFromString(BackupItem.serializer(), text)
                for (bookmark in parsed.bookmarkList) bcRepository.addBookmark(bookmark.toDomain())
                for (category in parsed.categoryList) bcRepository.addCategory(category.toDomain())
                true
            } catch (t: Throwable) {
                false
            }
            _uiState.value = BackupRestoreUiState(isWorking = false)
            _events.emit(if (ok) BackupRestoreEvent.RestoreSuccess else BackupRestoreEvent.RestoreFail)
        }
    }
}
