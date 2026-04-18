package com.illusion.checkfirm.domain.model

data class Preference(
    val profileName: String = "Unknown",
    val theme: String = "light",
    val isDynamicColorEnabled: Boolean = false,
    val language: String = "",
    val isQuickSearchBarEnabled: Boolean = false,
    val bookmarkOrder: String = "time",
    val isBookmarkAscOrder: Boolean = true,
    val isWelcomeSearchEnabled: Boolean = false,
    val isInfoCatcherEnabled: Boolean = false,
    val isFirebaseEnabled: Boolean = true
)
