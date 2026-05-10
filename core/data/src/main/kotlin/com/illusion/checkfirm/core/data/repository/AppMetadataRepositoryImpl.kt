package com.illusion.checkfirm.core.data.repository

import com.illusion.checkfirm.core.domain.model.ApiResponse
import com.illusion.checkfirm.core.domain.model.AppMetadata
import com.illusion.checkfirm.core.domain.remote.AppMetadataFetcher
import com.illusion.checkfirm.core.domain.repository.AppMetadataRepository
import jakarta.inject.Inject

class AppMetadataRepositoryImpl @Inject constructor(
    private val appMetadataFetcher: AppMetadataFetcher
) : AppMetadataRepository {

    override suspend fun fetchAppMetadata(): ApiResponse<AppMetadata> {
        return appMetadataFetcher.fetchAppMetadata()
    }
}