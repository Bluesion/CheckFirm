package com.illusion.checkfirm.core.domain.model

interface CheckFirmBuildConfig {
    val isDebug: Boolean
    val versionCode: Int
    val versionName: String
}
