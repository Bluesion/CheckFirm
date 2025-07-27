package com.illusion.checkfirm.data.model

data class SettingsItem(
    var profileName: String = "Unknown",
    var theme: String = "light",
    var isDynamicColorEnabled: Boolean = false,
    var language: String = "",
    var isQuickSearchBarEnabled: Boolean = false,
    var bookmarkOrder: String = "time",
    var isBookmarkAscOrder: Boolean = true,
    var isWelcomeSearchEnabled: Boolean = false,
    var isInfoCatcherEnabled: Boolean = false,
    var isFirebaseEnabled: Boolean = true
)