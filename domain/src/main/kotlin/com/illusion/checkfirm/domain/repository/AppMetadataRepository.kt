package com.illusion.checkfirm.domain.repository

import com.illusion.checkfirm.domain.model.ApiResponse
import com.illusion.checkfirm.domain.model.AppMetadata

interface AppMetadataRepository {
    suspend fun fetchAppMetadata(): ApiResponse<AppMetadata>
}
