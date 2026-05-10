package com.illusion.checkfirm.core.domain.repository

import com.illusion.checkfirm.core.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface WelcomeSearchRepository {
    val allDevices: Flow<List<Device>>
    suspend fun insert(device: Device)
    suspend fun delete(device: Device)
}
