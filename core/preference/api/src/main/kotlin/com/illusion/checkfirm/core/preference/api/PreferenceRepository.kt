package com.illusion.checkfirm.core.preference.api

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun getSettings(): Flow<Preference>
    suspend fun updateSettings(settings: Preference)
}
