package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.SettingsItem
import com.illusion.checkfirm.data.source.local.SettingsDataSource
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val settingsDataSource: SettingsDataSource
) {

    fun getAllSettings(): Flow<SettingsItem> {
        return settingsDataSource.getAllSettings
    }

    fun getProfileName(): Flow<String> {
        return settingsDataSource.getProfileName
    }

    suspend fun setProfileName(name: String) {
        settingsDataSource.setProfileName(name)
    }

    fun getTheme(): Flow<String> {
        return settingsDataSource.getTheme
    }

    suspend fun setTheme(theme: String) {
        settingsDataSource.setTheme(theme)
    }

    fun isDynamicColorEnabled(): Flow<Boolean> {
        return settingsDataSource.isDynamicColorEnabled
    }

    suspend fun enableDynamicColor(enable: Boolean) {
        settingsDataSource.enableDynamicColor(enable)
    }

    fun getAppLanguage(): Flow<String> {
        return settingsDataSource.getAppLanguage
    }

    suspend fun setAppLanguage(language: String) {
        settingsDataSource.setAppLanguage(language)
    }

    fun isQuickSearchBarEnabled(): Flow<Boolean> {
        return settingsDataSource.isQuickSearchBarEnabled
    }

    suspend fun enableQuickSearchBar(enable: Boolean) {
        settingsDataSource.enableQuickSearchBar(enable)
    }

    fun getBookmarkOrder(): Flow<String> {
        return settingsDataSource.getBookmarkOrder
    }

    suspend fun setBookmarkOrder(order: String) {
        settingsDataSource.setBookmarkOrder(order)
    }

    fun isBookmarkAscOrder(): Flow<Boolean> {
        return settingsDataSource.isBookmarkAscOrder
    }

    suspend fun setBookmarkAscOrder(isAscOrder: Boolean) {
        settingsDataSource.setBookmarkAscOrder(isAscOrder)
    }

    fun isWelcomeSearchEnabled(): Flow<Boolean> {
        return settingsDataSource.isWelcomeSearchEnabled
    }

    suspend fun enableWelcomeSearch(enable: Boolean) {
        settingsDataSource.enableWelcomeSearch(enable)
    }

    fun isInfoCatcherEnabled(): Flow<Boolean> {
        return settingsDataSource.isInfoCatcherEnabled
    }

    suspend fun enableInfoCatcher(enable: Boolean) {
        settingsDataSource.enableInfoCatcher(enable)
    }

    fun isFirebaseEnabled(): Flow<Boolean> {
        return settingsDataSource.isFirebaseEnabled
    }

    suspend fun enableFirebase(enable: Boolean) {
        settingsDataSource.enableFirebase(enable)
    }
}