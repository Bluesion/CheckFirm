package com.illusion.checkfirm

import com.illusion.checkfirm.domain.model.CheckFirmBuildConfig
import jakarta.inject.Inject

class CheckFirmBuildConfigImpl @Inject constructor() : CheckFirmBuildConfig {
    override val isDebug: Boolean = BuildConfig.DEBUG
    override val versionCode: Int = BuildConfig.VERSION_CODE
    override val versionName: String = BuildConfig.VERSION_NAME
}
