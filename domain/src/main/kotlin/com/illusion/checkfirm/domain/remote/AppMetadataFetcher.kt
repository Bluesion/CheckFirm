package com.illusion.checkfirm.domain.remote

import com.illusion.checkfirm.domain.model.ApiResponse
import com.illusion.checkfirm.domain.model.AppVersionStatus

interface AppMetadataFetcher {
    suspend fun checkAppVersion(currentAppVersion: Int): ApiResponse<AppVersionStatus>
}