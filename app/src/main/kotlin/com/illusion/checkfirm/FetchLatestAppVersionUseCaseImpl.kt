package com.illusion.checkfirm

import com.illusion.checkfirm.domain.model.ApiResponse
import com.illusion.checkfirm.domain.model.CheckFirmBuildConfig
import com.illusion.checkfirm.domain.repository.AppMetadataRepository
import com.illusion.checkfirm.feature.home.api.AppVersionStatus
import com.illusion.checkfirm.feature.home.api.FetchLatestAppVersionUseCase
import jakarta.inject.Inject

class FetchLatestAppVersionUseCaseImpl @Inject constructor(
    private val buildConfig: CheckFirmBuildConfig,
    private val appMetadataRepository: AppMetadataRepository,
) : FetchLatestAppVersionUseCase {
    override suspend operator fun invoke(): AppVersionStatus {
        return when (val appMetadata = appMetadataRepository.fetchAppMetadata()) {
            is ApiResponse.Success -> {
                val currentVersion = buildConfig.versionCode
                val latestVersion = appMetadata.data.appVersionData.latest.versionCode
                val minimumVersion = appMetadata.data.appVersionData.minimum.versionCode

                if (currentVersion >= latestVersion) {
                    AppVersionStatus.LATEST_VERSION
                } else {
                    if (currentVersion < minimumVersion) {
                        AppVersionStatus.UPDATE_REQUIRED
                    } else {
                        AppVersionStatus.OLD_VERSION
                    }
                }
            }

            is ApiResponse.Error -> {
                AppVersionStatus.LATEST_VERSION
            }
        }
    }
}
