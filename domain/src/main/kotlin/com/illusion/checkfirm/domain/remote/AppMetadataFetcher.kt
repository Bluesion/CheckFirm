package com.illusion.checkfirm.domain.remote

import com.illusion.checkfirm.domain.model.ApiResponse
import com.illusion.checkfirm.domain.model.AppMetadata

interface AppMetadataFetcher {
    suspend fun fetchAppMetadata(): ApiResponse<AppMetadata>
}
