package com.illusion.checkfirm.feature.settings.about

data class AboutUiState(
    val activeDialog: DialogType = DialogType.NONE,
    val versionCheck: VersionCheckState = VersionCheckState.Loading,
)

enum class DialogType {
    NONE, CONTRIBUTOR, LEGAL,
}

sealed interface VersionCheckState {
    data object Loading : VersionCheckState
    data object Latest : VersionCheckState
    data object Outdated : VersionCheckState
    data object NetworkError : VersionCheckState
}
