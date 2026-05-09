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

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val submitReportUseCase: SubmitReportUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ReportEvent>()
    val events = _events.asSharedFlow()

    fun toggleBugType(type: BugType) {
        _uiState.update {
            it.copy(
                bugTypes = if (type in it.bugTypes) it.bugTypes - type else it.bugTypes + type
            )
        }
    }

    fun updateUserMessage(userMessage: String) {
        _uiState.update { it.copy(userMessage = userMessage) }
    }

    fun submitReport() {
        val current = uiState.value
        if (current.bugTypes.isEmpty()) return

        _uiState.update { it.copy(isSubmitting = true) }
        viewModelScope.launch {
            val result = submitReportUseCase(
                bugTypes = current.bugTypes,
                userMessage = current.userMessage,
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
