package com.illusion.checkfirm.core.domain.repository

import com.illusion.checkfirm.core.domain.model.ApiResponse
import com.illusion.checkfirm.core.domain.model.AppMetadata

interface AppMetadataRepository {
    suspend fun fetchAppMetadata(): ApiResponse<AppMetadata>
}
