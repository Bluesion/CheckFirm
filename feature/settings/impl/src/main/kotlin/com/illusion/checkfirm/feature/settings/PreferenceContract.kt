package com.illusion.checkfirm.feature.settings

import com.illusion.checkfirm.core.preference.api.Preference

data class PreferenceUiState(
    val preference: Preference = Preference(),
    val activeDialog: PreferenceDialog = PreferenceDialog.None,
)

enum class PreferenceDialog {
    None, Profile, Theme, Language, BookmarkOrder, BookmarkReset
}
