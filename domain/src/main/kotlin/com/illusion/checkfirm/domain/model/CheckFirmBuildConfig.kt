package com.illusion.checkfirm.domain.model

interface CheckFirmBuildConfig {
    val isDebug: Boolean
    val versionCode: Int
    val versionName: String
}
