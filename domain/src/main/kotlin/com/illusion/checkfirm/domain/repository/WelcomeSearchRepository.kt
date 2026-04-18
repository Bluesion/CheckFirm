package com.illusion.checkfirm.domain.repository

import com.illusion.checkfirm.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface WelcomeSearchRepository {
    val allDevices: Flow<List<Device>>
    suspend fun insert(device: Device)
    suspend fun delete(device: Device)
}
