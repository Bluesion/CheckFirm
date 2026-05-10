package com.illusion.checkfirm.core.domain.model

data class AppMetadata(
    val appVersionData: AppVersionData,
)

data class AppVersionData(
    val latest: AppVersion,
    val minimum: AppVersion,
)

data class AppVersion(
    val versionName: String,
    val versionCode: Int,
    val versionDate: String,
)
