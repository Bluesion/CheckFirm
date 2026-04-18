package com.illusion.checkfirm.domain.remote

import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.model.OfficialFirmware
import com.illusion.checkfirm.domain.model.TestFirmware

interface FirmwareFetcher {
    suspend fun fetchOfficialFirmwareInfo(
        device: Device
    ): OfficialFirmware?

    suspend fun fetchTestFirmwareInfo(
        device: Device
    ): TestFirmware?
}