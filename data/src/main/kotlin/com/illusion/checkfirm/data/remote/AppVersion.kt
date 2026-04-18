package com.illusion.checkfirm.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppMetadataInfo(
    @SerialName("version_data") val appVersionData: AppVersionData
)

@Serializable
data class AppVersionData(
    val latest: AppVersion,
    val minimum: AppVersion
)

@Serializable
data class AppVersion(
    @SerialName("version_name") val versionName: String,
    @SerialName("version_code") val versionCode: Int,
    @SerialName("version_date") val versionDate: String
)