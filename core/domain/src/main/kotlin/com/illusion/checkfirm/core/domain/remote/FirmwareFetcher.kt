package com.illusion.checkfirm.core.domain.remote

import com.illusion.checkfirm.core.domain.model.Device
import com.illusion.checkfirm.core.domain.model.OfficialFirmware
import com.illusion.checkfirm.core.domain.model.TestFirmware

interface FirmwareFetcher {
    suspend fun fetchOfficialFirmwareInfo(
        device: Device
    ): OfficialFirmware?

    suspend fun fetchTestFirmwareInfo(
        device: Device
    ): TestFirmware?
}
