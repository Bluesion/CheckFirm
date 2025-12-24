package com.illusion.checkfirm.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.illusion.checkfirm.data.model.local.SettingsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    val getAllSettings: Flow<SettingsItem>
        get() =
            dataStore.data.map {
                SettingsItem(
                    profileName = it[PROFILE_NAME] ?: "Unknown",
                    theme = it[APPEARANCE_THEME] ?: "light",
                    isDynamicColorEnabled = it[APPEARANCE_DYNAMIC_COLOR] ?: false,
                    language = it[APPEARANCE_LANGUAGE] ?: "",
                    isQuickSearchBarEnabled = it[APPEARANCE_QUICK_SEARCH_BAR] ?: false,
                    bookmarkOrder = it[BOOKMARK_ORDER] ?: "time",
                    isBookmarkAscOrder = it[BOOKMARK_SHOW_IN_ASC] ?: true,
                    isWelcomeSearchEnabled = it[SEARCH_WELCOME_SEARCH] ?: false,
                    isInfoCatcherEnabled = it[SEARCH_INFO_CATCHER] ?: false,
                    isFirebaseEnabled = it[PREFERENCE_FIREBASE] ?: true
                )
            }

    val getProfileName: Flow<String>
        get() =
            dataStore.data.map {
                it[PROFILE_NAME] ?: "Unknown"
            }

    suspend fun setProfileName(name: String) {
        dataStore.edit {
            it[PROFILE_NAME] = name
        }
    }

    val getTheme: Flow<String>
        get() =
            dataStore.data.map {
                it[APPEARANCE_THEME] ?: "light"
            }

    suspend fun setTheme(theme: String) {
        dataStore.edit {
            it[APPEARANCE_THEME] = theme
        }
    }

    val isDynamicColorEnabled: Flow<Boolean>
        get() =
            dataStore.data.map {
                it[APPEARANCE_DYNAMIC_COLOR] ?: false
            }

    suspend fun enableDynamicColor(enable: Boolean) {
        dataStore.edit {
            it[APPEARANCE_DYNAMIC_COLOR] = enable
        }
    }

    val getAppLanguage: Flow<String>
        get() =
            dataStore.data.map {
                it[APPEARANCE_LANGUAGE] ?: ""
            }

    suspend fun setAppLanguage(language: String) {
        dataStore.edit {
            it[APPEARANCE_LANGUAGE] = language
        }
    }

    val isQuickSearchBarEnabled: Flow<Boolean>
        get() =
            dataStore.data.map {
                it[APPEARANCE_QUICK_SEARCH_BAR] ?: false
            }

    suspend fun enableQuickSearchBar(enable: Boolean) {
        dataStore.edit {
            it[APPEARANCE_QUICK_SEARCH_BAR] = enable
        }
    }

    val getBookmarkOrder: Flow<String>
        get() =
            dataStore.data.map {
                it[BOOKMARK_ORDER] ?: "time"
            }

    suspend fun setBookmarkOrder(order: String) {
        dataStore.edit {
            it[BOOKMARK_ORDER] = order
        }
    }

    val isBookmarkAscOrder: Flow<Boolean>
        get() =
            dataStore.data.map {
                it[BOOKMARK_SHOW_IN_ASC] ?: true
            }

    suspend fun setBookmarkAscOrder(isAscOrder: Boolean) {
        dataStore.edit {
            it[BOOKMARK_SHOW_IN_ASC] = isAscOrder
        }
    }

    val isWelcomeSearchEnabled: Flow<Boolean>
        get() =
            dataStore.data.map {
                it[SEARCH_WELCOME_SEARCH] ?: false
            }

    suspend fun enableWelcomeSearch(enable: Boolean) {
        dataStore.edit {
            it[SEARCH_WELCOME_SEARCH] = enable
        }
    }

    val isInfoCatcherEnabled: Flow<Boolean>
        get() =
            dataStore.data.map {
                it[SEARCH_INFO_CATCHER] ?: false
            }

    suspend fun enableInfoCatcher(enable: Boolean) {
        dataStore.edit {
            it[SEARCH_INFO_CATCHER] = enable
        }
    }

    val isFirebaseEnabled: Flow<Boolean>
        get() =
            dataStore.data.map {
                it[PREFERENCE_FIREBASE] ?: true
            }

    suspend fun enableFirebase(enable: Boolean) {
        dataStore.edit {
            it[PREFERENCE_FIREBASE] = enable
        }
    }

    companion object {
        val PROFILE_NAME = stringPreferencesKey("profile_name")
        val APPEARANCE_THEME = stringPreferencesKey("app_theme")
        val APPEARANCE_DYNAMIC_COLOR = booleanPreferencesKey("is_dynamic_color_enabled")
        val APPEARANCE_LANGUAGE = stringPreferencesKey("app_language")
        val APPEARANCE_QUICK_SEARCH_BAR = booleanPreferencesKey("is_quick_search_bar_enabled")
        val BOOKMARK_ORDER = stringPreferencesKey("bookmark_order")
        val BOOKMARK_SHOW_IN_ASC = booleanPreferencesKey("show_bookmark_ascending_order")
        val SEARCH_WELCOME_SEARCH = booleanPreferencesKey("is_welcome_search_enabled")
        val SEARCH_INFO_CATCHER = booleanPreferencesKey("is_info_catcher_enabled")
        val PREFERENCE_FIREBASE = booleanPreferencesKey("is_firebase_enabled")
    }
}