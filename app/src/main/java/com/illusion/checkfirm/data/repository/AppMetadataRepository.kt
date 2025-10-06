package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.remote.ApiResponse
import com.illusion.checkfirm.data.model.remote.AppVersionStatus
import com.illusion.checkfirm.data.source.remote.AppMetadataFetcher

class AppMetadataRepository(private val appMetadataFetcher: AppMetadataFetcher) {

    suspend fun checkAppVersion(): ApiResponse<AppVersionStatus> {
        return appMetadataFetcher.checkAppVersion()
    }
}