package com.illusion.checkfirm.core.domain.model

data class Firmware(
    var officialFirmware: OfficialFirmware = OfficialFirmware(),
    var testFirmware: TestFirmware = TestFirmware()
)

data class OfficialFirmware(
    var latestFirmware: String = "",
    var previousFirmware: Map<String, String> = emptyMap(),
    var deviceName: String = "",
    var releaseDate: String = "",
    var androidVersion: String = ""
)

data class TestFirmware(
    var latestFirmware: String = "",
    var previousFirmware: Map<String, String> = emptyMap(),
    var betaFirmware: Map<String, String> = emptyMap(),
    var discoveryDate: String = "",
    var firmwareUpdateType: FirmwareUpdateType? = null,
    var androidVersion: String = "",
    var isDowngradable: Boolean = false,
    var decryptedFirmware: String = "",
    var discoverer: String = "",
    var watson: String = "",
    var clue: String = ""
)

enum class FirmwareUpdateType {
    MAJOR, MINOR, ROLLBACK
}
