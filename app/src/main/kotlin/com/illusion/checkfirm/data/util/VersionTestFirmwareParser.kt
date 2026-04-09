package com.illusion.checkfirm.data.util

data class VersionTestFirmwareInfo(
    val latestFirmware: String,
    val androidVersion: String,
    val previousFirmware: List<String>
)

object VersionTestFirmwareParser {
    fun parse(xml: String): VersionTestFirmwareInfo? {
        if (xml.contains("<Error>")) {
            return null
        }

        val latestValue = LATEST_REGEX.find(xml)?.groupValues?.get(2)?.trim().orEmpty()
        val androidVersion = LATEST_REGEX.find(xml)?.groupValues?.get(1)?.trim().orEmpty()
        val values = buildList {
            val seen = LinkedHashSet<String>()
            VALUE_REGEX.findAll(xml).forEach { match ->
                val firmware = match.groupValues[1].trim()
                if (firmware.isNotEmpty() && seen.add(firmware)) {
                    add(firmware)
                }
            }
        }
        val latestFirmware =
            if (latestValue.isNotBlank() && !latestValue.isLikelyVersionTestHash()) {
                latestValue
            } else {
                ""
            }
        val previousFirmware = buildList {
            val seen = LinkedHashSet<String>()

            if (latestValue.isLikelyVersionTestHash() && seen.add(latestValue)) {
                add(latestValue)
            }

            values.forEach { firmware ->
                if (seen.add(firmware)) {
                    add(firmware)
                }
            }
        }

        if (latestFirmware.isBlank() && previousFirmware.isEmpty()) {
            return null
        }

        return VersionTestFirmwareInfo(
            latestFirmware = latestFirmware,
            androidVersion = androidVersion,
            previousFirmware = previousFirmware
        )
    }

    private fun String.isLikelyVersionTestHash(): Boolean {
        return length in setOf(32, 64) && all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }
    }

    private val LATEST_REGEX =
        Regex("""<latest(?:\s+o="([^"]*)")?\s*>(.*?)</latest>|<latest(?:\s+o="([^"]*)")?\s*/>""", RegexOption.DOT_MATCHES_ALL)

    private val VALUE_REGEX =
        Regex("""<value(?:\s+[^>]*)?>(.*?)</value>""", RegexOption.DOT_MATCHES_ALL)
}
