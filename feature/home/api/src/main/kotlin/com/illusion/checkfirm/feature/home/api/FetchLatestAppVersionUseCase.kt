package com.illusion.checkfirm.feature.home.api

interface FetchLatestAppVersionUseCase {
    suspend operator fun invoke(): AppVersionStatus
}