package com.illusion.checkfirm.core.preference.impl.data.repository

import com.illusion.checkfirm.core.preference.impl.local.preference.PreferenceManager
import com.illusion.checkfirm.domain.model.Preference
import com.illusion.checkfirm.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : PreferenceRepository {
    override fun getSettings(): Flow<Preference> = preferenceManager.getAllSettings

    override suspend fun updateSettings(settings: Preference) {
        preferenceManager.updateSettings(settings)
    }
}
