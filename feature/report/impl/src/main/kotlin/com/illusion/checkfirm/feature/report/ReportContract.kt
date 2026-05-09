package com.illusion.checkfirm.feature.report

data class ReportUiState(
    val bugTypes: Set<BugType> = emptySet(),
    val userMessage: String = "",
    val isSubmitting: Boolean = false,
)

enum class BugType {
    FIRMWARE_INFO_ERROR,
    INAPPROPRIATE_USER_NAME,
    SMART_SEARCH_INFO_ERROR,
    OTHER_ERROR,
}

sealed interface ReportEvent {
    data object SubmitSuccess : ReportEvent
    data object SubmitError : ReportEvent
}
