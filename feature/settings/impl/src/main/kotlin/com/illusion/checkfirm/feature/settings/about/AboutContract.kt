package com.illusion.checkfirm.feature.settings.about

data class AboutUiState(
    val activeDialog: DialogType = DialogType.NONE,
)

enum class DialogType {
    NONE, CONTRIBUTOR, LEGAL,
}
