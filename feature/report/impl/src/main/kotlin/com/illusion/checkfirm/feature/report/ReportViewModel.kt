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
    val bugType: String = "",
    val deviceDetails: String = "",
    val logs: String = "",
    val consentGiven: Boolean = false,
    val isSubmitting: Boolean = false
)

sealed interface ReportEvent {
    data class SubmitSuccess(val message: String) : ReportEvent
    data class SubmitError(val message: String) : ReportEvent
}

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val submitReportUseCase: SubmitReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ReportEvent>()
    val events = _events.asSharedFlow()

    fun updateBugType(type: String) {
        _uiState.update { it.copy(bugType = type) }
    }

    fun updateDeviceDetails(details: String) {
        _uiState.update { it.copy(deviceDetails = details) }
    }

    fun updateLogs(logs: String) {
        _uiState.update { it.copy(logs = logs) }
    }

    fun updateConsent(consent: Boolean) {
        _uiState.update { it.copy(consentGiven = consent) }
    }

    fun submitReport() {
        val currentState = uiState.value
        if (!currentState.consentGiven || currentState.bugType.isBlank() || currentState.deviceDetails.isBlank() || currentState.logs.isBlank()) {
            return
        }

        _uiState.update { it.copy(isSubmitting = true) }
        viewModelScope.launch {
            val result = submitReportUseCase(
                bugType = currentState.bugType,
                deviceDetails = currentState.deviceDetails,
                logs = currentState.logs
            )

            _uiState.update { it.copy(isSubmitting = false) }

            result.onSuccess {
                _events.emit(ReportEvent.SubmitSuccess("Report submitted successfully."))
                // clear form
                _uiState.update { ReportUiState() }
            }.onFailure {
                _events.emit(ReportEvent.SubmitError("Failed to submit report: ${it.message}"))
            }
        }
    }
}
