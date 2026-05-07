package com.illusion.checkfirm.feature.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.feature.report.domain.usecase.SubmitReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReportUiState(
    /** Multi-select: any combination of "type_1".."type_4". */
    val bugTypes: Set<String> = emptySet(),
    val logs: String = "",
    val isSubmitting: Boolean = false,
)

sealed interface ReportEvent {
    data object SubmitSuccess : ReportEvent
    data object SubmitError : ReportEvent
}

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val submitReportUseCase: SubmitReportUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ReportEvent>()
    val events = _events.asSharedFlow()

    fun toggleBugType(type: String) {
        _uiState.update {
            val next = if (type in it.bugTypes) it.bugTypes - type else it.bugTypes + type
            it.copy(bugTypes = next)
        }
    }

    fun updateLogs(logs: String) {
        _uiState.update { it.copy(logs = logs) }
    }

    fun submitReport(bugTypeLabels: Map<String, String>) {
        val current = uiState.value
        if (current.bugTypes.isEmpty()) return

        _uiState.update { it.copy(isSubmitting = true) }
        viewModelScope.launch {
            val labels = current.bugTypes.mapNotNull { bugTypeLabels[it] }
            val result = submitReportUseCase(
                bugTypeLabels = labels,
                logs = current.logs,
            )
            _uiState.update { it.copy(isSubmitting = false) }
            if (result.isSuccess) {
                _uiState.value = ReportUiState()
                _events.emit(ReportEvent.SubmitSuccess)
            } else {
                _events.emit(ReportEvent.SubmitError)
            }
        }
    }
}
