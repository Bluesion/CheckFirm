package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.local.SettingsItem
import com.illusion.checkfirm.data.source.local.DataStoreManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SettingsRepository {
    fun getAllSettings(): Flow<SettingsItem>
    fun getProfileName(): Flow<String>
    suspend fun setProfileName(name: String)
    fun getTheme(): Flow<String>
    suspend fun setTheme(theme: String)
    fun isDynamicColorEnabled(): Flow<Boolean>
    suspend fun enableDynamicColor(enable: Boolean)
    fun getAppLanguage(): Flow<String>
    suspend fun setAppLanguage(language: String)
    fun isQuickSearchBarEnabled(): Flow<Boolean>
    suspend fun enableQuickSearchBar(enable: Boolean)
    fun getBookmarkOrder(): Flow<String>
    suspend fun setBookmarkOrder(order: String)
    fun isBookmarkAscOrder(): Flow<Boolean>
    suspend fun setBookmarkAscOrder(isAscOrder: Boolean)
    fun isWelcomeSearchEnabled(): Flow<Boolean>
    suspend fun enableWelcomeSearch(enable: Boolean)
    fun isInfoCatcherEnabled(): Flow<Boolean>
    suspend fun enableInfoCatcher(enable: Boolean)
    fun isFirebaseEnabled(): Flow<Boolean>
    suspend fun enableFirebase(enable: Boolean)
}

class SettingsRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : SettingsRepository {

    override fun getAllSettings(): Flow<SettingsItem> {
        return dataStoreManager.getAllSettings
    }

    override fun getProfileName(): Flow<String> {
        return dataStoreManager.getProfileName
    }

    override suspend fun setProfileName(name: String) {
        dataStoreManager.setProfileName(name)
    }

    override fun getTheme(): Flow<String> {
        return dataStoreManager.getTheme
    }

    override suspend fun setTheme(theme: String) {
        dataStoreManager.setTheme(theme)
    }

    override fun isDynamicColorEnabled(): Flow<Boolean> {
        return dataStoreManager.isDynamicColorEnabled
    }

    override suspend fun enableDynamicColor(enable: Boolean) {
        dataStoreManager.enableDynamicColor(enable)
    }

    override fun getAppLanguage(): Flow<String> {
        return dataStoreManager.getAppLanguage
    }

    override suspend fun setAppLanguage(language: String) {
        dataStoreManager.setAppLanguage(language)
    }

    override fun isQuickSearchBarEnabled(): Flow<Boolean> {
        return dataStoreManager.isQuickSearchBarEnabled
    }

    override suspend fun enableQuickSearchBar(enable: Boolean) {
        dataStoreManager.enableQuickSearchBar(enable)
    }

    override fun getBookmarkOrder(): Flow<String> {
        return dataStoreManager.getBookmarkOrder
    }

    override suspend fun setBookmarkOrder(order: String) {
        dataStoreManager.setBookmarkOrder(order)
    }

    override fun isBookmarkAscOrder(): Flow<Boolean> {
        return dataStoreManager.isBookmarkAscOrder
    }

    override suspend fun setBookmarkAscOrder(isAscOrder: Boolean) {
        dataStoreManager.setBookmarkAscOrder(isAscOrder)
    }

    override fun isWelcomeSearchEnabled(): Flow<Boolean> {
        return dataStoreManager.isWelcomeSearchEnabled
    }

    override suspend fun enableWelcomeSearch(enable: Boolean) {
        dataStoreManager.enableWelcomeSearch(enable)
    }

    override fun isInfoCatcherEnabled(): Flow<Boolean> {
        return dataStoreManager.isInfoCatcherEnabled
    }

    override suspend fun enableInfoCatcher(enable: Boolean) {
        dataStoreManager.enableInfoCatcher(enable)
    }

    override fun isFirebaseEnabled(): Flow<Boolean> {
        return dataStoreManager.isFirebaseEnabled
    }

    override suspend fun enableFirebase(enable: Boolean) {
        dataStoreManager.enableFirebase(enable)
    }
}