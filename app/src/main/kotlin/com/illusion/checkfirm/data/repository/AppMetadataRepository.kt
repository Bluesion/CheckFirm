package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.remote.ApiResponse
import com.illusion.checkfirm.data.model.remote.AppVersionStatus
import com.illusion.checkfirm.data.source.remote.AppMetadataFetcher
import javax.inject.Inject

interface AppMetadataRepository {
    suspend fun checkAppVersion(): ApiResponse<AppVersionStatus>
}

class AppMetadataRepositoryImpl @Inject constructor(
    private val appMetadataFetcher: AppMetadataFetcher
) : AppMetadataRepository {

    override suspend fun checkAppVersion(): ApiResponse<AppVersionStatus> {
        return appMetadataFetcher.checkAppVersion()
    }
}