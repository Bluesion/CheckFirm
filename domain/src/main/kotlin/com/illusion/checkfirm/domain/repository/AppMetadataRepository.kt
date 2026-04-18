package com.illusion.checkfirm.domain.repository

import com.illusion.checkfirm.domain.model.ApiResponse
import com.illusion.checkfirm.domain.model.AppVersionStatus

interface AppMetadataRepository {
    suspend fun checkAppVersion(currentAppVersion: Int): ApiResponse<AppVersionStatus>
}