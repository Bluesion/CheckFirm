package com.illusion.checkfirm.core.domain.remote

import com.illusion.checkfirm.core.domain.model.ApiResponse
import com.illusion.checkfirm.core.domain.model.AppMetadata

interface AppMetadataFetcher {
    suspend fun fetchAppMetadata(): ApiResponse<AppMetadata>
}
