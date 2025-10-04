package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.local.SettingsItem
import com.illusion.checkfirm.data.source.local.DataStoreManager
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val dataStoreManager: DataStoreManager
) {

    fun getAllSettings(): Flow<SettingsItem> {
        return dataStoreManager.getAllSettings
    }

    fun getProfileName(): Flow<String> {
        return dataStoreManager.getProfileName
    }

    suspend fun setProfileName(name: String) {
        dataStoreManager.setProfileName(name)
    }

    fun getTheme(): Flow<String> {
        return dataStoreManager.getTheme
    }

    suspend fun setTheme(theme: String) {
        dataStoreManager.setTheme(theme)
    }

    fun isDynamicColorEnabled(): Flow<Boolean> {
        return dataStoreManager.isDynamicColorEnabled
    }

    suspend fun enableDynamicColor(enable: Boolean) {
        dataStoreManager.enableDynamicColor(enable)
    }

    fun getAppLanguage(): Flow<String> {
        return dataStoreManager.getAppLanguage
    }

    suspend fun setAppLanguage(language: String) {
        dataStoreManager.setAppLanguage(language)
    }

    fun isQuickSearchBarEnabled(): Flow<Boolean> {
        return dataStoreManager.isQuickSearchBarEnabled
    }

    suspend fun enableQuickSearchBar(enable: Boolean) {
        dataStoreManager.enableQuickSearchBar(enable)
    }

    fun getBookmarkOrder(): Flow<String> {
        return dataStoreManager.getBookmarkOrder
    }

    suspend fun setBookmarkOrder(order: String) {
        dataStoreManager.setBookmarkOrder(order)
    }

    fun isBookmarkAscOrder(): Flow<Boolean> {
        return dataStoreManager.isBookmarkAscOrder
    }

    suspend fun setBookmarkAscOrder(isAscOrder: Boolean) {
        dataStoreManager.setBookmarkAscOrder(isAscOrder)
    }

    fun isWelcomeSearchEnabled(): Flow<Boolean> {
        return dataStoreManager.isWelcomeSearchEnabled
    }

    suspend fun enableWelcomeSearch(enable: Boolean) {
        dataStoreManager.enableWelcomeSearch(enable)
    }

    fun isInfoCatcherEnabled(): Flow<Boolean> {
        return dataStoreManager.isInfoCatcherEnabled
    }

    suspend fun enableInfoCatcher(enable: Boolean) {
        dataStoreManager.enableInfoCatcher(enable)
    }

    fun isFirebaseEnabled(): Flow<Boolean> {
        return dataStoreManager.isFirebaseEnabled
    }

    suspend fun enableFirebase(enable: Boolean) {
        dataStoreManager.enableFirebase(enable)
    }
}