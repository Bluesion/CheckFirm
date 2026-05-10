package com.illusion.checkfirm.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.illusion.checkfirm.core.preference.api.Preference
import com.illusion.checkfirm.core.preference.api.PreferenceRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PreferenceRepository {

    override fun getSettings(): Flow<Preference> {
        return dataStore.data.map {
            Preference(
                profileName = it[PROFILE_NAME] ?: "Unknown",
                theme = it[APPEARANCE_THEME] ?: "system",
                language = it[APPEARANCE_LANGUAGE] ?: "",
                isQuickSearchBarEnabled = it[APPEARANCE_QUICK_SEARCH_BAR] ?: false,
                bookmarkOrder = it[BOOKMARK_ORDER] ?: "time",
                isBookmarkAscOrder = it[BOOKMARK_SHOW_IN_ASC] ?: true,
                isWelcomeSearchEnabled = it[SEARCH_WELCOME_SEARCH] ?: false,
                isInfoCatcherEnabled = it[SEARCH_INFO_CATCHER] ?: false,
                isFirebaseEnabled = it[PREFERENCE_FIREBASE] ?: true
            )
        }
    }

    override suspend fun updateSettings(settings: Preference) {
        dataStore.edit {
            it[PROFILE_NAME] = settings.profileName
            it[APPEARANCE_THEME] = settings.theme
            it[APPEARANCE_LANGUAGE] = settings.language
            it[APPEARANCE_QUICK_SEARCH_BAR] = settings.isQuickSearchBarEnabled
            it[BOOKMARK_ORDER] = settings.bookmarkOrder
            it[BOOKMARK_SHOW_IN_ASC] = settings.isBookmarkAscOrder
            it[SEARCH_WELCOME_SEARCH] = settings.isWelcomeSearchEnabled
            it[SEARCH_INFO_CATCHER] = settings.isInfoCatcherEnabled
            it[PREFERENCE_FIREBASE] = settings.isFirebaseEnabled
        }
    }

    companion object {
        val PROFILE_NAME = stringPreferencesKey("profile_name")
        val APPEARANCE_THEME = stringPreferencesKey("app_theme")
        val APPEARANCE_LANGUAGE = stringPreferencesKey("app_language")
        val APPEARANCE_QUICK_SEARCH_BAR = booleanPreferencesKey("is_quick_search_bar_enabled")
        val BOOKMARK_ORDER = stringPreferencesKey("bookmark_order")
        val BOOKMARK_SHOW_IN_ASC = booleanPreferencesKey("show_bookmark_ascending_order")
        val SEARCH_WELCOME_SEARCH = booleanPreferencesKey("is_welcome_search_enabled")
        val SEARCH_INFO_CATCHER = booleanPreferencesKey("is_info_catcher_enabled")
        val PREFERENCE_FIREBASE = booleanPreferencesKey("is_firebase_enabled")
    }
}