package com.illusion.checkfirm.domain.repository

import com.illusion.checkfirm.domain.model.Preference
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun getSettings(): Flow<Preference>
    suspend fun updateSettings(settings: Preference)
}
