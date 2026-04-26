package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.domain.model.ApiResponse
import com.illusion.checkfirm.domain.model.AppVersionStatus
import com.illusion.checkfirm.domain.remote.AppMetadataFetcher
import com.illusion.checkfirm.domain.repository.AppMetadataRepository
import jakarta.inject.Inject

class AppMetadataRepositoryImpl @Inject constructor(
    private val appMetadataFetcher: AppMetadataFetcher
) : AppMetadataRepository {

    override suspend fun checkAppVersion(currentAppVersion: Int): ApiResponse<AppVersionStatus> {
        return appMetadataFetcher.checkAppVersion(currentAppVersion)
    }
}