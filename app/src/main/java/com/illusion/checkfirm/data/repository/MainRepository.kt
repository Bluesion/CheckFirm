package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.remote.ApiResponse
import com.illusion.checkfirm.data.model.remote.AppVersionStatus
import com.illusion.checkfirm.data.source.remote.VersionFetcher

class MainRepository(private val versionFetcher: VersionFetcher) {

    suspend fun checkAppVersion(): ApiResponse<AppVersionStatus> {
        return versionFetcher.checkAppVersion()
    }
}